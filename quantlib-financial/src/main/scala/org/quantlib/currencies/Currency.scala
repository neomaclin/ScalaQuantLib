package org.quantlib.currencies


import cats.Eq
import org.quantlib.math.Rounding
import org.quantlib.math.Rounding._

final case class Currency(name: String,
                          code: String,
                          numericCode: Int,
                          symbol: String,
                          fractionSymbol: String,
                          fractionPerUnit: Int,
                          triangulationCurrency: Option[Currency] = None,
                          rounding: Rounding = NoRounding())
object Currency {

  implicit object EqCurrency extends Eq[Currency]{
    override def eqv(x: Currency, y: Currency): Boolean = x.numericCode == y.numericCode
  }

}