package org.quantlib.termstructures.inflation


import org.quantlib.time.Period
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.enums.Frequency
import org.quantlib.time.enums.Frequency._
import org.quantlib.time.enums.TimeUnit._
import org.quantlib.time.implicits.DateOps
import org.quantlib.time.implicits.DateOps._

trait Seasonality {

  def correctZeroRate[D: DateOps](date: D, r: Double, its: InflationTermStructure[D]): Double

  def correctYoYRate[D: DateOps](date: D, r: Double, its: InflationTermStructure[D]): Double

  def isConsistent: Boolean
}

final case class MultiplicativePriceSeasonality[D: DateOps](seasonalityBaseDate: D,
                                                frequency: Frequency,
                                                seasonalityFactors: Seq[Double]) extends Seasonality {
  frequency match {
    case Semiannual | EveryFourthMonth | Quarterly | Bimonthly | Monthly | Biweekly | Weekly | Daily =>
      require(seasonalityFactors.size % frequency.value == 0,
        s"For frequency $frequency require multiple of ${frequency.value} factors ${seasonalityFactors.size} were given.")
    case _ =>
      require(requirement = false, s"bad frequency specified: $frequency, only semi-annual through daily permitted.")
  }

  def correctZeroRate[D: DateOps](date: D, r: Double,its: InflationTermStructure[D]): Double = {
    val (_, curveBaseDate) = its.inflationPeriod(its.baseDate, its.frequency)

    seasonalityCorrection(r, date, its.dayCounter, curveBaseDate, true)
  }

  def correctYoYRate[D: DateOps](date: D, r: Double,its: InflationTermStructure[D]): Double = {
    val (_, curveBaseDate) = InflationTermStructure.inflationPeriod(its.baseDate, its.frequency)

    seasonalityCorrection(r, date, its.dayCounter, curveBaseDate, false);
  }

  def isConsistent: Boolean = {
    if (frequency == Daily) {
      true
    } else if (frequency.value == seasonalityFactors.size) {
      true
    } else {

      // how many years do you need to test?
      val nTest = seasonalityFactors.size / frequency.value
      // ... relative to the start of the inflation curve
      val (_, curveBaseDate) = period(its.baseDate, its.frequency)
      val factorBase = seasonalityFactor(curveBaseDate)

      val eps = 0.00001

      1 until nTest forall { i =>
        Math.abs(seasonalityFactor(curveBaseDate.plusYears(i)) - factorBase) < eps
      }
    }
  }

  def seasonalityFactor(to: D): Double = {

    val from = seasonalityBaseDate
    val nFactors = seasonalityFactors.size
    val factorPeriod = Period(frequency)
    val which = if (from == to) {
      0
    } else {
      // days, weeks, months, years are the only time unit possibilities
      val diffDays: Int = Math.abs(to - from)
      // in days
      val dir = if (from > to) -1 else 1
      val diff = factorPeriod.unit match {
        case Days =>
          dir * diffDays
        case Weeks =>
          dir * (diffDays / 7)
        case Months =>
          val (startDate, endDate) = InflationTermStructure.inflationPeriod(to, frequency)
          var diff = diffDays / (31 * factorPeriod.length)
          var go = from + dir * diff * factorPeriod
          while (!(startDate <= go && go <= endDate)) {
            go += (dir * factorPeriod)
            diff += 1
          }
          dir * diff
        case Years => Int.MaxValue
        case _ => Int.MaxValue
      }

      // now adjust to the available number of factors, direction dependent
      if (dir == 1) diff % nFactors else (nFactors - (-diff % nFactors)) % nFactors

    }

    seasonalityFactors(which)
  }

  def seasonalityCorrection(rate: Double,
                            atDate: D,
                            dc: DayCountBasis[D],
                            curveBaseDate: D,
                            isZeroRate: Boolean): Double = {
    val factorAt = seasonalityFactor(atDate)

    //Getting seasonality correction for either ZC or YoY
    val f =
      if (isZeroRate) {
        val factorBase = seasonalityFactor(curveBaseDate)
        val seasonalityAt = factorAt / factorBase
        val timeFromCurveBase = dc.yearFraction(curveBaseDate, atDate)

        Math.pow(seasonalityAt, 1 / timeFromCurveBase)
      }
      else {
        val factor1Ybefore = seasonalityFactor(atDate - Period(1, Years))

        factorAt / factor1Ybefore
      }

    (rate + 1) * f - 1
  }

}

final case class KerkhofSeasonality[D: DateOps](seasonalityBaseDate: D,
                                                seasonalityFactors: Seq[Double]) extends Seasonality {
  frequency match {
    case Semiannual | EveryFourthMonth | Quarterly | Bimonthly | Monthly | Biweekly | Weekly | Daily =>
      require(seasonalityFactors.size % frequency.value == 0,
        s"For frequency $frequency require multiple of ${frequency.value} factors ${seasonalityFactors.size} were given.")
    case _ =>
      require(requirement = false, s"bad frequency specified: $frequency, only semi-annual through daily permitted.")
  }

  def correctZeroRate[D: DateOps](date: D, r: Double,its: InflationTermStructure[D]): Double = {
    val (_, curveBaseDate) = InflationTermStructure.inflationPeriod(its.baseDate, its.frequency)

    seasonalityCorrection(r, date, its.dayCounter, curveBaseDate, true)
  }

  def correctYoYRate[D: DateOps](date: D, r: Double,its: InflationTermStructure[D]): Double = {
    val (_, curveBaseDate) = InflationTermStructure.inflationPeriod(its.baseDate, its.frequency)

    seasonalityCorrection(r, date, its.dayCounter, curveBaseDate, false);
  }

  def isConsistent: Boolean = {
    if (frequency == Daily) {
      true
    } else if (frequency.value == seasonalityFactors.size) {
      true
    } else {

      // how many years do you need to test?
      val nTest = seasonalityFactors.size / frequency.value
      // ... relative to the start of the inflation curve
      val (_, curveBaseDate) = period(its.baseDate, its.frequency)
      val factorBase = seasonalityFactor(curveBaseDate)

      val eps = 0.00001

      1 until nTest forall { i =>
        Math.abs(seasonalityFactor(curveBaseDate.plusYears(i)) - factorBase) < eps
      }
    }
  }

  def seasonalityFactor(to: D): Double = {

    val from = seasonalityBaseDate
    val nFactors = seasonalityFactors.size
    val factorPeriod = Period(frequency)
    val which = if (from == to) {
      0
    } else {
      // days, weeks, months, years are the only time unit possibilities
      val diffDays: Int = Math.abs(to - from)
      // in days
      val dir = if (from > to) -1 else 1
      val diff = factorPeriod.unit match {
        case Days =>
          dir * diffDays
        case Weeks =>
          dir * (diffDays / 7)
        case Months =>
          val (startDate, endDate) = InflationTermStructure.inflationPeriod(to, frequency)
          var diff = diffDays / (31 * factorPeriod.length)
          var go = from + dir * diff * factorPeriod
          while (!(startDate <= go && go <= endDate)) {
            go += (dir * factorPeriod)
            diff += 1
          }
          dir * diff
        case Years => Int.MaxValue
        case _ => Int.MaxValue
      }

      // now adjust to the available number of factors, direction dependent
      if (dir == 1) diff % nFactors else (nFactors - (-diff % nFactors)) % nFactors

    }

    seasonalityFactors(which)
  }

  def seasonalityCorrection(rate: Double,
                            atDate: D,
                            dc: DayCountBasis[D],
                            curveBaseDate: D,
                            isZeroRate: Boolean): Double = {
    val factorAt = seasonalityFactor(atDate)

    //Getting seasonality correction for either ZC or YoY
    val f =
      if (isZeroRate) {
        val factorBase = seasonalityFactor(curveBaseDate)
        val seasonalityAt = factorAt / factorBase
        val timeFromCurveBase = dc.yearFraction(curveBaseDate, atDate)

        Math.pow(seasonalityAt, 1 / timeFromCurveBase)
      }
      else {
        val factor1Ybefore = seasonalityFactor(atDate - Period(1, Years))

        factorAt / factor1Ybefore
      }

    (rate + 1) * f - 1
  }

}
