package org.quantlib.instruments.swaps

import java.time.LocalDate

import org.quantlib._

/**
  * Created by neo on 31/03/2017.
  */
object AssetSwap {

  final case class Request(fixedResetDates: Seq[LocalDate],
                           fixedPayDates: Seq[LocalDate],
                           fixedCoupons: Seq[Double],
                           floatingAccrualTimes: Seq[Time],
                           floatingResetDates: Seq[LocalDate],
                           floatingFixingDates: Seq[LocalDate],
                           floatingPayDates: Seq[LocalDate],
                           floatingSpreads:Seq[Spread]){
    require(fixedResetDates.size == fixedPayDates.size,
      "number of fixed start dates different from number of fixed payment dates")
    require(fixedPayDates.size == fixedCoupons.size,
      "number of fixed payment dates different from number of fixed coupon amounts")
    require(floatingResetDates.size == floatingPayDates.size,
      "number of floating start dates different from number of floating payment dates")
    require(floatingFixingDates.size == floatingPayDates.size,
      "number of floating fixing dates different from number of floating payment dates")
    require(floatingAccrualTimes.size == floatingPayDates.size,
      "number of floating accrual times different from number of floating payment dates")
    require(floatingSpreads.size == floatingPayDates.size,
      "number of floating spreads different from number of floating payment dates")
  }

  final case class Result(fairSpread: Spread,
                          fairCleanPrice: Double,
                          fairNonParRepayment: Double)
}

