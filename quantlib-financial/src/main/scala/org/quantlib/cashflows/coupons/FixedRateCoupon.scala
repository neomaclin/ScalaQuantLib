package org.quantlib.cashflows.coupons

import java.time.LocalDate

import org.quantlib.InterestRate
import org.quantlib.time.{Period, Schedule}
import org.quantlib.time.calendars.BusinessCalendar
import org.quantlib.time.calendars.BusinessCalendar._
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.enums.BusinessDayConvention
import org.quantlib.time.enums.BusinessDayConvention.Following
import org.quantlib.time.implicits.DateOps


/**
  * Created by neo on 02/04/2017.
  */
object FixedRateCoupon {

  def legFrom[D: DateOps](schedule: Schedule[D],
                          notionals: Seq[Double],
                          couponRates: Seq[InterestRate[D]],
                          paymentAdjustment: BusinessDayConvention = Following,
                          firstPeriodDayCounter: DayCountBasis[D],
                          paymentCalendar: BusinessCalendar[D],
                          exCouponPeriod: Option[(Period,
                            BusinessCalendar[D],
                            BusinessDayConvention,
                            Boolean)]): Seq[FixedRateCoupon[D]] = {
    require(couponRates.nonEmpty, "no coupon rates given")
    require(notionals.nonEmpty, "no notional given")
    require(schedule.dates.length >= 2, "no enough dates in the schedule to generate meaningful leg")
    require(schedule.isRegular.nonEmpty, "no enough dates in the schedule to generate meaningful leg")
    require(couponRates.length == notionals.length)
    require(notionals.length == schedule.isRegular.length)

    val firstPeriod = schedule.dates.take(2)
    val midRange = schedule.dates.slice(2, schedule.dates.length - 1)
    val lastPeriod = schedule.dates.takeRight(2)

    val firstCoupon = (firstPeriod.init zip firstPeriod.tail).map { case (start, end) =>
      val paymentDate = paymentCalendar.adjust(end, paymentAdjustment)
      val exCouponDate = exCouponPeriod.map {
        case (tenor, exCouponCalendar, exCouponAdjustment, exCouponEndOfMonth) =>
          exCouponCalendar.advance(paymentDate, -tenor, exCouponAdjustment, exCouponEndOfMonth)
      }

      val refStart =
        if (schedule.isRegular.head) {
          require(firstPeriodDayCounter == couponRates.head.dc, "regular first coupon does not allow a first-period day count")
          start
        } else {
          schedule.calendar.advance(end, -schedule.tenor, schedule.convention, schedule.endOfMonth)
        }
      FixedRateCoupon[D](paymentDate, notionals.head, couponRates.head, start, end, Some(refStart), Some(end), exCouponDate)
    }

    val lastCoupon = (lastPeriod.init zip lastPeriod.tail).map { case (start, end) =>
      val paymentDate = paymentCalendar.adjust(end, paymentAdjustment)
      val exCouponDate = exCouponPeriod.map {
        case (tenor, exCouponCalendar, exCouponAdjustment, exCouponEndOfMonth) =>
          exCouponCalendar.advance(paymentDate, -tenor, exCouponAdjustment, exCouponEndOfMonth)
      }
      val refEnd =
        if (schedule.isRegular.last) {
          end
        } else {
          schedule.calendar.advance(start, schedule.tenor, schedule.convention, schedule.endOfMonth)
        }
      FixedRateCoupon[D](paymentDate, notionals.last, couponRates.last, start, end, Some(start), Some(refEnd), exCouponDate)
    }

    if (schedule.dates.length > 2) {
      if (midRange.nonEmpty) {
        val regularCoupons = (midRange.init zip midRange.tail zip couponRates.init.tail zip notionals.init.tail) map {
          case (((start, end), rate), nominal) =>
            val paymentDate = paymentCalendar.adjust(end, paymentAdjustment)
            val exCouponDate = exCouponPeriod.map {
              case (tenor, exCouponCalendar, exCouponAdjustment, exCouponEndOfMonth) =>
                exCouponCalendar.advance(paymentDate, -tenor, exCouponAdjustment, exCouponEndOfMonth)
            }
            FixedRateCoupon[D](paymentDate, nominal, rate, start, end, Some(start), Some(end), exCouponDate)
        }

        firstCoupon ++ regularCoupons ++ lastCoupon
      } else {
        firstCoupon ++ lastCoupon
      }
    } else {
      firstCoupon
    }

  }
}

final case class FixedRateCoupon[D: DateOps](paymentDate: D,
                                             nominal: Double,
                                             interestRate: InterestRate[D],
                                             accrualStartDate: D,
                                             accrualEndDate: D,
                                             refPeriodStart: Option[D],
                                             refPeriodEnd: Option[D],
                                             exCouponDate: Option[D])