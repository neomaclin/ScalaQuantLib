package org.quantlib.currencies

import org.quantlib.time.implicits.DateOps

object Money {

  sealed trait ConversionType

  case class BaseCurrencyConversion(base: Currency) extends ConversionType

  case object AutomatedConversion extends ConversionType

//  def convert[D: DateOps](x: Money, y: Money)(implicit exchangeRateManager: ExchangeRateManager[D]) = {
//
//  }
//  private def relationalOperation(x: Money, y: Money, conversionType: ConversionType): Boolean =
//    if (x.currency == y.currency) operator(x.value, y.value)
//    else conversionType match {
//      case BaseCurrencyConversion(base) => conoperator(x.convert(base).value, y.convert(base).value)
//      case AutomatedConversion => operator(y.convert(x.currency).value, x.value)
//    }
//
//  implicit object MoneyProximity extends Proximity[Money] {
//    def ~=(x: Money, y: Money, size: Int): Boolean = relationalOperation(x, y)(_ ~= _)
//  }
//
//  implicit object MoneyRelational extends Equality[Money] with Order[Money] {
//    def ==(x: Money, y: Money): Boolean = relationalOperation(x, y)(_ == _)
//
//    def >(x: Money, y: Money): Boolean = relationalOperation(x, y)(_ > _)
//
//    def <(x: Money, y: Money): Boolean = relationalOperation(x, y)(_ < _)
//  }



}

final case class Money(value: Double = 0.0, currency: Currency) {

  def rounded: Money = Money(currency.rounding(value), currency)

  def unary_- : Money = Money(-this.value, this.currency)

  def +(other: Money): Money = copy(value = other.value + this.value)

  def /(other: Money): Money = copy(value = other.value / this.value)

  def -(other: Money): Money = copy(value = other.value - this.value)

  def *(other: Money): Money = copy(value = other.value * this.value)

}
