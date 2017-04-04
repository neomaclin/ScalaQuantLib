package org.quantlib.instruments

import java.time.LocalDate


/**
  * Created by neo on 15/10/2016.
  */

object Claim {

  case object FaceValueClaim extends Claim {

    def amount(defaultDate: LocalDate, notional: Double, recoveryRate: Double): Double = {
      notional * (1.0 - recoveryRate)
    }

  }
//
//  final case class FaceValueAccrualClaim(bond: Bond) extends Claim {
//
//    def amount(defaultDate: LocalDate, notional: Double, recoveryRate: Double): Double = {
//      val accrual = bond.accruedAmount(defaultDate) / bond.notionalAt(defaultDate)
//      notional * (1.0 - recoveryRate - accrual)
//    }
//  }

}

sealed trait Claim {
  def amount(defaultDate: LocalDate, notional: Double, recoveryRate: Double): Double
}
