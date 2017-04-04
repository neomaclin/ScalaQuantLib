package org.quantlib.enums

/**
  * Created by neo on 20/03/2017.
  */
sealed trait Position

object Position {

  case object Long extends Position

  case object Short extends Position

}