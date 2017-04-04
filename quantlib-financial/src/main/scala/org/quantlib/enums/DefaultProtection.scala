package org.quantlib.enums

object DefaultProtection {
  sealed trait Side
  object Side {

    case object Buyer extends Side

    case object Seller extends Side

  }
}
