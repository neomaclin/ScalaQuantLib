package org.quantlib.cashflows.coupons

import org.quantlib.InterestRate
import org.quantlib.cashflows.coupons.CPI.InterpolationType
import org.quantlib.time.Period
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 02/04/2017.
  */
object InflationCoupon {

}

trait ZeroInflationIndex

trait YoYInflationIndex

//final case class InflationCoupon[D: DateOps, I: InflationIndex](paymentDate: D,
//                                             nominal: Double,
//                                             interestRate: InterestRate[D],
//                                             accrualStartDate: D,
//                                             accrualEndDate: D,
//                                             refPeriodStart: Option[D],
//                                             refPeriodEnd: Option[D],
//                                             exCouponDate: Option[D],
//                                             fixingDays: Int,
//                                             index: I,
//                                             observationLag: Period,
//                                             observationInterpolation: InterpolationType,
//                                             dayCounter: DayCountBasis[D])

final case class CPICoupon[D: DateOps](baseCPI: Double, // user provided
                                       paymentDate: D,
                                       nominal: Double,
                                       interestRate: InterestRate[D],
                                       accrualStartDate: D,
                                       accrualEndDate: D,
                                       refPeriodStart: Option[D],
                                       refPeriodEnd: Option[D],
                                       exCouponDate: Option[D],
                                       fixingDays: Int,
                                       zeroIndex: ZeroInflationIndex,
                                       observationLag: Period,
                                       observationInterpolation: InterpolationType,
                                       dayCounter: DayCountBasis[D],
                                       fixedRate: Double,
                                       spread: Double)


final case class CappedFlooredYoYInflationCoupon[D: DateOps](paymentDate: D,
                                                             nominal: Double,
                                                             interestRate: InterestRate[D],
                                                             accrualStartDate: D,
                                                             accrualEndDate: D,
                                                             refPeriodStart: Option[D],
                                                             refPeriodEnd: Option[D],
                                                             exCouponDate: Option[D],
                                                             fixingDays: Int,
                                                             observationLag: Period,
                                                             yoyIndex: YoYInflationIndex,
                                                             observationInterpolation: InterpolationType,
                                                             dayCounter: DayCountBasis[D],
                                                             fixedRate: Double,
                                                             spread: Double,
                                                             cap: Double,
                                                             floor: Double)
