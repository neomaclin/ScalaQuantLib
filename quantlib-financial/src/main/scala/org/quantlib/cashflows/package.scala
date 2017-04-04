package org.quantlib

import java.time.LocalDate


/**
  * Created by neo on 20/03/2017.
  */
package object cashflows {

  trait Event[E] {

  }

  trait CashFlow[C] {

  }

  implicit class CashFlowOps[C: CashFlow](val cashFlow: C) {

  }

  final case class SimpleCashFlow(amount: Double, date: LocalDate)

  final case class Redemption(amount: Double, date: LocalDate)

  final case class AmortizingPayment(amount: Double, date: LocalDate)

  //type Leg[C: CashFlowOps] = Seq[C]

  implicit object RedemptionFlow extends CashFlow[Redemption]

}
