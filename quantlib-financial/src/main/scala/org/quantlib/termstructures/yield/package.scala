package org.quantlib.termstructures

import org.quantlib.InterestRate
import org.quantlib.enums.Compounding
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.enums.Frequency

/**
  * Created by neo on 02/04/2017.
  */
package object `yield` {
  trait YieldTermStructureT[T] extends TermStructureT[T] {

    def discount[D](ofYTS: T, d: D, extrapolate: Boolean): Double

    def zeroRate[D](ofYTS: T, d: D,
                    resultDayCounter: Option[DayCountBasis[D]],
                    comp: Compounding,
                    freq: Frequency,
                    extrapolate: Boolean): InterestRate[D]

    def forwardRate[D](ofYTS: T,
                       d1: D,
                       d2: D,
                       resultDayCounter: Option[DayCountBasis[D]],
                       comp: Compounding,
                       freq: Frequency,
                       extrapolate: Boolean): InterestRate[D]
  }

  implicit class YieldTermStructureOps[T: YieldTermStructureT](val ts: T){
    val impl = implicitly[YieldTermStructureT[T]]
    //def discount[D](d: D, extrapolate: Boolean) = impl.discount()
  }
}
