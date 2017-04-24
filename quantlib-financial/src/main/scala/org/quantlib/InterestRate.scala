package org.quantlib


import org.quantlib.enums.Compounding
import org.quantlib.enums.Compounding._
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.enums.Frequency
import org.quantlib.time.enums.Frequency._
import org.quantlib.time.implicits.DateOps
import org.quantlib.time.implicits.DateOps._


final case class InterestRate[D: DateOps](rate: Double,
                                          dc: DayCountBasis[D],
                                          comp: Compounding,
                                          freq: Frequency) {

  import InterestRate._

  if (comp == Compounded || comp == SimpleThenCompounded || comp == CompoundedThenSimple)
    require(freq != Once && freq != NoFrequency, "frequency not allowed for this interest rate")

  def discountFactor(time: Double): Double = 1.0 / compoundFactor(time)

  def discountFactor(d1: D, d2: D, refStart: Option[D] = None, refEnd: Option[D] = None): Double = {
    require(d2 >= d1, s"date1($d1) later than date2($d2)")

    discountFactor(dc.yearFraction(d1, d2, refStart, refEnd))
  }

  def compoundFactor(time: Double): Double = {
    require(time >= 0.0, "negative time not allowed")

    def simple = 1.0 + rate * time

    def compounded = Math.pow(1.0 + rate / freq.value, freq.value * time)

    comp match {
      case Simple => simple
      case Compounded => compounded
      case Continuous => Math.exp(rate * time)
      case SimpleThenCompounded => if (time <= 1.0 / freq.value) simple else compounded
      case CompoundedThenSimple => if (time > 1.0 / freq.value) simple else compounded
    }
  }

  def compoundFactor(d1: D, d2: D, refStart: Option[D] = None, refEnd: Option[D] = None): Double = {
    require(d2 >= d1, s"date1($d1) later than date2($d2)")

    compoundFactor(dc.yearFraction(d1, d2, refStart, refEnd))
  }

  def equivalentRate(comp: Compounding, freq: Frequency, time: Double): InterestRate[D] = {
    impliedRate(compoundFactor(time), dc, comp, freq, time)
  }

  def equivalentRate(resultDc: DayCountBasis[D],
                     comp: Compounding,
                     freq: Frequency,
                     date1: D,
                     date2: D,
                     refStart: Option[D] = None,
                     refEnd: Option[D] = None): InterestRate[D] = {
    require(date2 >= date1, s"date1($date1) later than date2($date2)")

    val t1 = dc.yearFraction(date1, date2, refStart, refEnd)
    val t2 = resultDc.yearFraction(date1, date2, refStart, refEnd)

    impliedRate(compoundFactor(t1), resultDc, comp, freq, t2)
  }

  override def toString: String = {

    val compounding = comp match {
      case Simple => Simple
      case Compounded => freq
      case Continuous => Continuous
      case SimpleThenCompounded => "Simple Compounding up to " + (12 / freq.value) + " months, then " + freq
      case CompoundedThenSimple => "Simple Compounding up to " + (12 / freq.value) + " months, then " + freq
    }

    rate + " " + dc + " " + compounding + "Compounding"
  }

}

object InterestRate {

  def impliedRate[D: DateOps](compound: Double,
                              dc: DayCountBasis[D],
                              comp: Compounding,
                              freq: Frequency,
                              time: Double): InterestRate[D] = {

    require(compound > 0.0, "positive compound factor required")
    if (compound == 1.0) {
      require(time >=  0.0, s"non negative time ($time) required when compound is 1")
    } else{
      require(time > 0.0, s"positive time ($time) required")
    }


    def simpleRate = (compound - 1.0) / time

    def compoundedRate = (Math.pow(compound, 1.0 / (freq.value.toDouble * time)) - 1.0) * freq.value.toDouble

    val rate =
      if (compound == 1.0) 0.0
      else comp match {
        case Simple => simpleRate
        case Compounded => compoundedRate
        case Continuous => Math.log(compound) / time
        case SimpleThenCompounded => if (time <= 1.0 / freq.value.toDouble) simpleRate else compoundedRate
        case CompoundedThenSimple => if (time > 1.0 / freq.value.toDouble) simpleRate else compoundedRate
      }


    InterestRate[D](rate, dc, comp, freq)
  }

  def impliedRate[D: DateOps](compound: Double,
                              dc: DayCountBasis[D],
                              comp: Compounding,
                              freq: Frequency,
                              date1: D, date2: D,
                              refStart: Option[D] = None, refEnd: Option[D] = None): InterestRate[D] = {
    require(date2 >= date1, s"date1($date1) later than date2($date2)")

    impliedRate[D](compound, dc, comp, freq, dc.yearFraction(date1, date2, refStart, refEnd))
  }

}
