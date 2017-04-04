package org.quantlib.cashflows

/**
  * Created by neo on 02/04/2017.
  */
object Duration {

  sealed trait Type

  object Type{
    case object Simple extends Type
    case object Macaulay extends Type
    case object Modified extends Type
  }

}

