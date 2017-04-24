package org.quantlib.currencies

import java.time.LocalDate

import cats.kernel.Order
import org.quantlib.currencies.Money.ConversionType
import org.quantlib.math.Comparison.Proximity
import org.quantlib.math.Comparing._


object Money {

  implicit class DoubleArithmeticOpsWithMoney(val number: Double) extends AnyVal {
    def *(other: Money): Money = other.copy(value = other.value * number)

    def +(other: Money): Money = other.copy(value = other.value + number)

    def /(other: Money): Money = other.copy(value = number / other.value)

    def -(other: Money): Money = other.copy(value = number - other.value)

  }

  implicit object MoneyOrder extends Order[Money] {

    override def compare(x: Money, y: Money): Int = x.value.compareTo(y.value)
  }

  implicit object MoneyProximity extends Proximity[Money] {
    def ~=(x: Money, y: Money)(implicit size: Int): Boolean = {
      if (x == y) {
        true
      } else {
        if (x.currency == y.currency) x.value ~= y.value
        else {
          false
        }
      }
    }

    def =~(x: Money, y: Money)(implicit size: Int): Boolean = {
      if (x == y) {
        true
      } else {
        if (x.currency == y.currency) x.value =~ y.value
        else {
          false
        }
      }
    }

  }

  sealed trait ConversionType

  object ConversionType {

    case object NoConversion extends ConversionType

    case class BaseCurrencyConversion(base: Currency) extends ConversionType

    case object AutomatedConversion extends ConversionType

  }


}

final case class Money(value: Double = 0.0, currency: Currency) {

  private def convert(right: Money)
                     (operation: (Double, Double) => Double)
                     (implicit conversionType: ConversionType,
                      exchangeManager: ExchangeRateManager[LocalDate],
                      date: LocalDate,
                      exchangeType: ExchangeRate.Type): Money = {

    conversionType match {
      case ConversionType.NoConversion => copy(value = operation(this.value, right.value))
      case ConversionType.BaseCurrencyConversion(base: Currency) =>

        val leftConverted = exchangeManager.lookup(this.currency, base, date, exchangeType).map(_.exchange(this).rounded)
        val rightConverted = exchangeManager.lookup(right.currency, base, date, exchangeType).map(_.exchange(right).rounded)

        require(leftConverted.isDefined, s"Unable to convert $this to $base")
        require(rightConverted.isDefined, s"Unable to convert $right to $base")

        Money(operation(leftConverted.get.value, rightConverted.get.value), base)
      case ConversionType.AutomatedConversion =>
        val converted = exchangeManager.lookup(right.currency, this.currency, date, exchangeType).map(_.exchange(right).rounded)
        require(converted.isDefined, s"Unable to convert $right to $currency")
        copy(value = operation(value, converted.get.value))
    }

  }

  def rounded: Money = Money(currency.rounding(value), currency)

  def unary_- : Money = Money(-this.value, this.currency)

  def *(other: Money)(implicit conversionType: ConversionType,
                      exchangeManager: ExchangeRateManager[LocalDate] ,
                      date: LocalDate ,
                      exchangeType: ExchangeRate.Type ): Money = convert(other)(_ * _)


  def +(other: Money)(implicit conversionType: ConversionType,
                      exchangeManager: ExchangeRateManager[LocalDate] ,
                      date: LocalDate ,
                      exchangeType: ExchangeRate.Type  ): Money  = convert(other)(_ + _)

  def /(other: Money)(implicit conversionType: ConversionType,
                      exchangeManager: ExchangeRateManager[LocalDate] ,
                      date: LocalDate ,
                      exchangeType: ExchangeRate.Type  ): Money  = convert(other)(_ / _)

  def -(other: Money)(implicit conversionType: ConversionType,
                      exchangeManager: ExchangeRateManager[LocalDate] ,
                      date: LocalDate ,
                      exchangeType: ExchangeRate.Type  ): Money  =  convert(other)(_ - _)


  def *(other: Double): Money = copy(value = other * this.value)

  def +(other: Double): Money = copy(value = other + this.value)

  def /(other: Double): Money = copy(value = this.value / other)

  def -(other: Double): Money = copy(value = this.value - other)


}
