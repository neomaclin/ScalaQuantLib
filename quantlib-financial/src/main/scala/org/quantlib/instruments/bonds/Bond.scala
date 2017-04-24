//package org.quantlib.instruments.bonds
//
//import java.time.LocalDate
//
//import org.quantlib.cashflows._
//import org.quantlib.enums.Compounding
//import org.quantlib.time.Schedule
//import org.quantlib.time.calendars.BusinessCalendar
//import org.quantlib.time.calendars.BusinessCalendar._
//import org.quantlib.time.daycounts.DayCountBasis
//import org.quantlib.time.enums.Frequency
//import org.quantlib.time.implicits.DateOps
//
///**
//  * Created by neo on 02/04/2017.
//  */
//trait BondT[B] {
//
//}
//
//trait BondPrice[B] {
//  def cleanPrice(ofBond: B): Double
//
//  def dirtyPrice(ofBond: B): Double
//
//  def cleanPrice[D](ofBond: B,
//                    ofYield: Double,
//                    dc: DayCountBasis[D],
//                    comp: Compounding,
//                    freq: Frequency): Double
//
//  def dirtyPrice[D](ofBond: B,
//                    ofYield: Double,
//                    dc: DayCountBasis[D],
//                    comp: Compounding,
//                    freq: Frequency): Double
//}
//
//trait BondYield[B] {
//  def bondYield[D](ofBond: B,
//                   dc: DayCountBasis[D],
//                   comp: Compounding,
//                   freq: Frequency,
//                   accuracy: Double,
//                   maxEvaluations: Int): Double
//
//  def bondYield[D](ofBond: B,
//                   cleanPrice: Double,
//                   dc: DayCountBasis[D],
//                   comp: Compounding,
//                   freq: Frequency,
//                   settlementDate: D,
//                   accuracy: Double,
//                   maxEvaluations: Int): Double
//
//}
//
//object Bond {
//
//  //final case class Request[D: DateOps](settlementDate: D, cashflows: Leg[D], calendar: BusinessCalendar[D])
//
//  final case class Result(settlementValue: Double)
//
//
//  def ofZeroCoupon(principal: Double,
//                   issueDate: LocalDate,
//                   startDate: LocalDate,
//                   maturityDate: LocalDate,
//                   redemptionPercent: Double = 100.0,
//                   paymentSchedule: Schedule[LocalDate],
//                   calendar: BusinessCalendar[LocalDate]
//                  ): ZeroCouponBond = {
//
//    val redemptionDate = BusinessCalendar.adjust(calendar, maturityDate, paymentSchedule.convention)
//
//    val redemption = Redemption(principal * redemptionPercent/100.0, redemptionDate)
//
//    val bond =
//      BondStructure[Nothing](principal = principal,
//        issueDate = issueDate,
//        startDate = startDate,
//        maturityDate = maturityDate,
//        paymentSchedule = paymentSchedule,
//        calendar = calendar,
//        coupons = Nil,
//        notionals = Nil,
//        redemptions = Seq(redemption)
//      )
//    ZeroCouponBond(bond)
//  }
//
//}
//
//
//final case class BondStructure[C](principal: Double,
//                                  issueDate: LocalDate,
//                                  startDate: LocalDate,
//                                  maturityDate: LocalDate,
//                                  paymentSchedule: Schedule[LocalDate],
//                                  calendar: BusinessCalendar[LocalDate],
//                                  coupons: Leg[C],
//                                  notionals: Seq[Double],
//                                  redemptions: Leg[Redemption])
//
//final case class ZeroCouponBond(bond: BondStructure[Nothing]) {
//  require(bond.coupons.isEmpty, "ZeroCouponBond do not contains any coupon")
//  require(bond.redemptions.length == 1, "ZeroCouponBond has only one redemption amount")
//}
//
