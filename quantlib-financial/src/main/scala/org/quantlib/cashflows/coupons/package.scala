package org.quantlib.cashflows


import org.quantlib.InterestRate
import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 20/03/2017.
  */
package object coupons {

  object CPI{

    sealed trait InterpolationType
    object InterpolationType {

      case object AsIndex extends InterpolationType

      case object Flat extends InterpolationType

      case object Linear extends InterpolationType
     }
  }

  object Replication{

    sealed trait Type
    object Type {

      case object Sub extends Type

      case object Central extends Type

      case object Super extends Type
    }
  }


}
