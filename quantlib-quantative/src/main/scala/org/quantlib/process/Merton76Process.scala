package org.quantlib.process

/**
  * Created by neo on 26/02/2017.
  */
trait Quote
trait BlackVolTermStructure
final case class Merton76Process(stateVariable: Quote,
                               //  dividendTS: YieldTermStructure,
                               //  riskFreeTS: YieldTermStructure,
                                 blackVolTS: BlackVolTermStructure,
                                 jumpInt: Quote,
                                 logJMean: Quote,
                                 logJVol: Quote)
extends StochasticProcess1D {
  override def variance(t0: Double, x0: Double, dt: Double): Double = ???

  override def initialValue: Double = ???

  override def drift(t: Double, x: Double): Double = ???

  override def diffusion(t: Double, x: Double): Double = ???

  override def expectation(t0: Double, x0: Double, dt: Double): Double = ???

  override def stdDeviation(t0: Double, x0: Double, dt: Double): Double = ???
}
