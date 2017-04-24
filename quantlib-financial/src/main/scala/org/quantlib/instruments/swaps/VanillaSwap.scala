//package org.quantlib.instruments.swaps
//
//import java.time.LocalDate
//
////import org.quantlib.cashflows.Leg
//import org.quantlib.cashflows.coupons.FixedRateCoupon
//import org.quantlib.enums.Compounding
//import org.quantlib.time.Schedule
//import org.quantlib.time.calendars.BusinessCalendar
//import org.quantlib.time.daycounts.DayCountBasis
//import org.quantlib.time.enums.BusinessDayConvention.Following
//import org.quantlib.time.enums.{BusinessDayConvention, Frequency}
//import org.quantlib.{InterestRate, Spread, Time}
//
///**
//  * Created by neo on 02/04/2017.
//  */
//object VanillaSwap {
//
//  final case class Request(fixedResetDates: Seq[LocalDate],
//                           fixedPayDates: Seq[LocalDate],
//                           fixedCoupons: Seq[Double],
//                           floatingAccrualTimes: Seq[Time],
//                           floatingResetDates: Seq[LocalDate],
//                           floatingFixingDates: Seq[LocalDate],
//                           floatingPayDates: Seq[LocalDate],
//                           floatingSpreads: Seq[Spread],
//                           floatingCoupons: Seq[Double]) {
//    require(fixedResetDates.size == fixedPayDates.size,
//      "number of fixed start dates different from number of fixed payment dates")
//    require(fixedPayDates.size == fixedCoupons.size,
//      "number of fixed payment dates different from number of fixed coupon amounts")
//    require(floatingResetDates.size == floatingPayDates.size,
//      "number of floating start dates different from number of floating payment dates")
//    require(floatingFixingDates.size == floatingPayDates.size,
//      "number of floating fixing dates different from number of floating payment dates")
//    require(floatingAccrualTimes.size == floatingPayDates.size,
//      "number of floating accrual times different from number of floating payment dates")
//    require(floatingSpreads.size == floatingPayDates.size,
//      "number of floating spreads different from number of floating payment dates")
//  }
//
//  final case class Result(fairSpread: Spread, fairRate: Double)
//
//  def ofFixedLeg(nominal: Double,
//                 fixedSchedule: Schedule[LocalDate],
//                 fixedRate: Double,
//                 fixedDayCount: DayCountBasis[LocalDate],
//                 fixedCompounding: Compounding,
//                 fixedFrequency: Frequency,
//                 calendar: BusinessCalendar[LocalDate],
//                 paymentConvention: BusinessDayConvention) = {
//
//    val notionals = Seq(nominal)
//    val rates = Seq(InterestRate(fixedRate, fixedDayCount, fixedCompounding, fixedFrequency))
//
//    FixedRateCoupon.legFrom(
//      schedule = fixedSchedule,
//      notionals = notionals,
//      couponRates = rates,
//      paymentAdjustment = paymentConvention,
//      firstPeriodDayCounter = fixedDayCount,
//      paymentCalendar = calendar,
//      exCouponPeriod = None)
//  }
//
//  //def ofFloatingLeg(nominal: Double, fixedSchedule: Schedule)
//
//
//}
//
//final case class VanillaSwap[I](side: Side.Type,
//                                fixedLeg: Leg[I],
//                                floatingLeg: Leg[I])