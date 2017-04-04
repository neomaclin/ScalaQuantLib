package org.quantlib.cashflows

import java.time.LocalDate

/**
  * Created by neo on 02/04/2017.
  */
object Dividend {
  final case class FixedDividend(amount: Double, date: LocalDate)
  final case class FractionalDividend(amount: Double, date: LocalDate)


}
