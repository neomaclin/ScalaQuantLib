package org.quantlib.process

/**
  * Created by neo on 26/02/2017.
  */
final case class SquareRootProcess( b: Double,
                                    a: Double,
                                    sigma: Double,
                                    x0: Double = 0.0) extends StochasticProcess1D {

  override def variance(t0: Double, x0: Double, dt: Double): Double = ???

  override def factors: Int = ???

  override def initialValue: Double = x0

  override def drift(t: Double, x: Double): Double = a * (b - x)

  override def diffusion(t: Double, x: Double): Double = sigma * Math.sqrt(x)

  override def expectation(t0: Double, x0: Double, dt: Double): Double = ???

  override def stdDeviation(t0: Double, x0: Double, dt: Double): Double = ???


}
