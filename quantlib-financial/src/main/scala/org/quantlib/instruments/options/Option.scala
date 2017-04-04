package org.quantlib.instruments.options

import org.quantlib.instruments.{Exercise, Payoff}

/**
  * Created by neo on 20/03/2017.
  */
object Option {

  sealed abstract class Type(val value: Int) {
    def other: Type
  }

  object Type {

    case object Call extends Type(1) {
      def other = Put
    }

    case object Put extends Type(-1) {
      def other = Call
    }

  }

  object Barrier {

    sealed trait Type

    object Type {

      case object DownIn extends Type

      case object UpIn extends Type

      case object DownOut extends Type

      case object UpOut extends Type

    }

  }

  object Average {

    sealed trait Type

    object Type {

      case object Arithmetic extends Type

      case object Geometric extends Type

    }

  }

  final case class Greeks(delta: Double,
                          gamma: Double,
                          theta: Double,
                          vega: Double,
                          rho: Double,
                          dividendRho: Double)

  final case class MoreGreeks(itmCashProbability: Double,
                              deltaForward: Double,
                              elasticity: Double,
                              thetaPerDay: Double,
                              strikeSensitivity: Double)

}

final case class Option[D](payoff: Payoff, exercise: Exercise[D])