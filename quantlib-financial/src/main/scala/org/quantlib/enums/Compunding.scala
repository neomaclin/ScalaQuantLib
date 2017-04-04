package org.quantlib.enums

/**
  * Created by neo on 20/03/2017.
  */
sealed trait Compounding

object Compounding {

  case object Simple extends Compounding

  case object Compounded extends Compounding

  case object Continuous extends Compounding

  case object SimpleThenCompounded extends Compounding

}
