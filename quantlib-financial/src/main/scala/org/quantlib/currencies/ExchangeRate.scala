package org.quantlib.currencies

import org.quantlib.currencies.ExchangeType._

sealed trait ExchangeType
object ExchangeType{

  case object Direct extends ExchangeType
  final case class Derived(rate1: ExchangeRate, rate2: ExchangeRate) extends ExchangeType

}

final case class ExchangeRate(source: Currency,
                              target: Currency,
                              rate: Double,
                              exchangeType: ExchangeType = Direct) {

  def exchange(money: Money): Money = {
    exchangeType match {
      case ExchangeType.Direct =>
        money.currency match {
          case `source` => Money(money.value * rate, target)
          case `target` => Money(money.value / rate, source)
          case _ => money
        }
      case ExchangeType.Derived(rate1, rate2) =>
        money.currency match {
          case `rate1`.source | `rate1`.target => rate2.exchange(rate1.exchange(money))
          case `rate2`.source | `rate2`.target => rate1.exchange(rate2.exchange(money))
          case _ => money
        }
    }
  }

  def chain(other: ExchangeRate): Option[ExchangeRate] = {
    if (this.source == other.source) {
      Some(ExchangeRate(this.target, other.target, other.rate / this.rate, exchangeType))
    } else if (this.source == other.target) {
      Some(ExchangeRate(this.target, other.source, 1.0 / (this.rate * other.rate), exchangeType))
    } else if (this.target == other.source) {
      Some(ExchangeRate(this.source, other.target, this.rate * other.rate, exchangeType))
    } else if (this.target == other.target) {
      Some(ExchangeRate(this.source, other.source, this.rate / other.rate, exchangeType))
    } else {
      None
    }
  }

}
