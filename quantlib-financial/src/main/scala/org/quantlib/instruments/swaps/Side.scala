package org.quantlib.instruments.swaps

/**
  * Created by neo on 02/04/2017.
  */

object Side {

  sealed abstract class Type(val value: Int)

  object Type {

    case object Receiver extends Type(-1)

    case object Payer extends Type(1)

  }

}
