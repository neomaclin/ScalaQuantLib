package org.quantlib.process

/**
  * Created by neo on 14/03/2017.
  */
trait HestonProcess extends StochasticProcessND{


  // probability densitiy function,
  // semi-analytical solution of the Fokker-Planck equation in x=ln(s)
  def pdf(x: Double, v: Double, t:Double,  eps: Double =1e-3): Double
}

object HestonProcess{
  sealed trait Discretization

  object Discretization {

    case object PartialTruncation extends Discretization

    case object FullTruncation extends Discretization

    case object Reflection extends Discretization

    case object NonCentralChiSquareVariance extends Discretization

    case object QuadraticExponential extends Discretization

    case object QuadraticExponentialMartingale extends Discretization

    case object BroadieKayaExactSchemeLobatto extends Discretization

    case object BroadieKayaExactSchemeLaguerre extends Discretization

    case object BroadieKayaExactSchemeTrapezoidal extends Discretization

  }
}