package org.quantlib

import java.time.LocalDate

import org.quantlib.math.Rounding._
import org.quantlib.enums.Compounding
import org.quantlib.enums.Compounding._
import org.quantlib.time.Period
import org.quantlib.time.daycounts.Actual
import org.quantlib.time.enums.{Frequency, TimeUnit}
import org.quantlib.time.enums.Frequency._
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.quantlib.time.implicits.DateOps
import org.quantlib.time.implicits.DateOps._

/**
  * Created by neo on 15/04/2017.
  */
class InterestRateSpec extends  PropSpec with TableDrivenPropertyChecks with Matchers {

  final case class InterestRateTestData(rate: Double,
                                        comp: Compounding,
                                        freq: Frequency,
                                        t: Double,
                                        comp2: Compounding,
                                        freq2: Frequency,
                                        expected: Double,
                                        precision: Int)

  val testCases = Table(
    "InterestRateTestData",
    InterestRateTestData(0.0800, Compounded,        Quarterly,   1.00, Continuous,            Annual, 0.0792, 4),
    InterestRateTestData(0.1200, Continuous,           Annual,   1.00, Compounded,            Annual, 0.1275, 4),
    InterestRateTestData(0.0800, Compounded,        Quarterly,   1.00, Compounded,            Annual, 0.0824, 4),
    InterestRateTestData(0.0700, Compounded,        Quarterly,   1.00, Compounded,        Semiannual, 0.0706, 4),
    // undocumented, but reasonable :)
    InterestRateTestData(0.0100, Compounded,           Annual,   1.00,     Simple,            Annual, 0.0100, 4),
    InterestRateTestData(0.0200,     Simple,           Annual,   1.00, Compounded,            Annual, 0.0200, 4),
    InterestRateTestData(0.0300, Compounded,       Semiannual,   0.50,     Simple,            Annual, 0.0300, 4),
    InterestRateTestData(0.0400,     Simple,           Annual,   0.50, Compounded,        Semiannual, 0.0400, 4),
    InterestRateTestData(0.0500, Compounded, EveryFourthMonth,  1.0/3,     Simple,            Annual, 0.0500, 4),
    InterestRateTestData(0.0600,     Simple,           Annual,  1.0/3, Compounded,  EveryFourthMonth, 0.0600, 4),
    InterestRateTestData(0.0500, Compounded,        Quarterly,   0.25,     Simple,            Annual, 0.0500, 4),
    InterestRateTestData(0.0600,     Simple,           Annual,   0.25, Compounded,         Quarterly, 0.0600, 4),
    InterestRateTestData(0.0700, Compounded,        Bimonthly,  1.0/6,     Simple,            Annual, 0.0700, 4),
    InterestRateTestData(0.0800,     Simple,           Annual,  1.0/6, Compounded,         Bimonthly, 0.0800, 4),
    InterestRateTestData(0.0900, Compounded,          Monthly, 1.0/12,     Simple,            Annual, 0.0900, 4),
    InterestRateTestData(0.1000,     Simple,           Annual, 1.0/12, Compounded,           Monthly, 0.1000, 4),

    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.25,               Simple,            Annual, 0.0300, 4),
    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.25,               Simple,        Semiannual, 0.0300, 4),
    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.25,               Simple,         Quarterly, 0.0300, 4),
    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.50,               Simple,            Annual, 0.0300, 4),
    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.50,               Simple,        Semiannual, 0.0300, 4),
    InterestRateTestData(0.0300, SimpleThenCompounded,       Semiannual,   0.75,           Compounded,        Semiannual, 0.0300, 4),

    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.25, SimpleThenCompounded,         Quarterly, 0.0400, 4),
    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.25, SimpleThenCompounded,        Semiannual, 0.0400, 4),
    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.25, SimpleThenCompounded,            Annual, 0.0400, 4),

    InterestRateTestData(0.0400,           Compounded,        Quarterly,   0.50, SimpleThenCompounded,         Quarterly, 0.0400, 4),
    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.50, SimpleThenCompounded,        Semiannual, 0.0400, 4),
    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.50, SimpleThenCompounded,            Annual, 0.0400, 4),

    InterestRateTestData(0.0400,           Compounded,        Quarterly,   0.75, SimpleThenCompounded,         Quarterly, 0.0400, 4),
    InterestRateTestData(0.0400,           Compounded,       Semiannual,   0.75, SimpleThenCompounded,        Semiannual, 0.0400, 4),
    InterestRateTestData(0.0400,               Simple,       Semiannual,   0.75, SimpleThenCompounded,            Annual, 0.0400, 4)
  )

  import org.quantlib.time.implicits.Date._

  val date1 = DateOps.now

  property("Interest Rate compound factor is the inverse of the discount factor") {
    forAll(testCases) { case InterestRateTestData(rate, comp, freq, t,_,_,_,_) =>
      val interestRate = InterestRate(rate, Actual(360), comp, freq)
      val date2 = date1 + Period((360 * t + 0.5).toInt, TimeUnit.Days)
      
      val compoundFactor = interestRate.compoundFactor(date1, date2)
      val discountFactor = interestRate.discountFactor(date1, date2)

      Math.abs(discountFactor - 1.0 / compoundFactor) should be <= 1e-15
    }
  }

  property("Interest Rates with *same* daycount basis, compounding, and frequency is the *same* InterestRate") {
    forAll(testCases) { case InterestRateTestData(rate, comp, freq, t,_,_,_,_) =>
      val interestRate = InterestRate(rate, Actual(360), comp, freq)
      val date2 = date1 + Period((360 * t + 0.5).toInt, TimeUnit.Days)
      val calculated = interestRate.equivalentRate(interestRate.dc, interestRate.comp, interestRate.freq, date1, date2)

      interestRate.dc shouldBe calculated.dc
      interestRate.comp shouldBe calculated.comp
      interestRate.freq shouldBe calculated.freq

      Math.abs(interestRate.rate - calculated.rate) should be <= 1e-15

    }
  }

  property("Interest Rates with *different* daycount basis, compounding, and frequency is the *expected* InterestRate") {
    forAll(testCases) { case InterestRateTestData(rate, comp, freq, t,comp2,freq2,expected,precision) =>

      val interestRate = InterestRate(rate, Actual(360), comp, freq)
      val date2 = date1 + Period((360 * t + 0.5).toInt, TimeUnit.Days)

      val calculatedInterestRate  = interestRate.equivalentRate(interestRate.dc, comp2, freq2, date1, date2)
      val expectedInterestRate = InterestRate(expected,interestRate.dc, comp2, freq2)

      val roundingPrecision = ClosestRounding(precision)
      val calculatedRate = roundingPrecision(calculatedInterestRate.rate)

      expectedInterestRate.dc shouldBe calculatedInterestRate.dc
      expectedInterestRate.comp shouldBe calculatedInterestRate.comp
      expectedInterestRate.freq shouldBe calculatedInterestRate.freq

      Math.abs(expectedInterestRate.rate - calculatedRate) should be <= 1e-17
    }
  }

  property("Interest Rates with *different* compounding, and frequency is the *expected* InterestRate") {
    forAll(testCases) { case InterestRateTestData(rate, comp, freq, t,comp2,freq2,expected,precision) =>

      val interestRate = InterestRate(rate, Actual(360), comp, freq)
      val date2 = date1 + Period((360 * t + 0.5).toInt, TimeUnit.Days)

      val calculatedInterestRate = interestRate.equivalentRate(interestRate.dc, comp2, freq2, date1, date2)

      val roundingPrecision = ClosestRounding(precision)
      val calculatedRate = roundingPrecision(calculatedInterestRate.rate)

      Math.abs(expected - calculatedRate) should be <= 1e-17

    }
  }

}
