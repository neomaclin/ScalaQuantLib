package org.quantlib.enums


import org.quantlib.enums.Price.Type._

/**
  * Created by neo on 02/04/2017.
  */
object Price {

  sealed trait Type

  object Type {

    final case class Bid(value: Double) extends Type

    final case class Ask(value: Double) extends Type

    final case class Last(value: Double) extends Type

    final case class Close(value: Double) extends Type

    final case class Mid(value: Double) extends Type

    final case class MidEquivalent(value: Double) extends Type

    final case class MidSafe(value: Double) extends Type

  }

  def midEquivalent(bid: Bid, ask: Ask, last: Last, close: Close): MidEquivalent = {
    val value = if (bid.value > 0.0) {
      if (ask.value > 0.0) (bid.value + ask.value) / 2.0 else bid.value
    } else {
      if (ask.value > 0.0) ask.value
      else if (last.value > 0.0) last.value
      else {
        require(close.value > 0.0, "all input prices are invalid")
        close.value
      }
    }
    MidEquivalent(value)
  }

  def midSafe(bid: Bid, ask: Ask): MidSafe = {
    require(bid.value > 0.0, "invalid bid price")
    require(ask.value > 0.0, "invalid ask price")
    MidSafe((bid.value + ask.value) / 2.0)
  }

}

object IntervalPrice {

  sealed trait Type

  object Type {

    final case class Open(value: Double) extends Type

    final case class Close(value: Double) extends Type

    final case class High(value: Double) extends Type

    final case class Low(value: Double) extends Type

  }



}