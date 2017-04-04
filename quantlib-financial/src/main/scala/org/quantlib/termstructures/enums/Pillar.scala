package org.quantlib.termstructures.enums

/**
  * Created by neo on 19/03/2017.
  */
object Pillar {
 sealed trait  Choice
 object Choice{
   case object MaturityDate extends Choice        //! instruments maturity date
   case object LastRelevantDate extends Choice    //! last date relevant for instrument pricing
   case object CustomDate extends Choice          //! custom choice
  }
}
