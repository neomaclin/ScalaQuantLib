package org.quantlib.process

/**
  * Created by neo on 26/02/2017.
  */
final case class OrnsteinUhlenbeckProcess(speed: Double,
                                          vol: Double,
                                          x0: Double = 0.0,
                                          level: Double = 0.0)
  extends StochasticProcess1D {
  require(vol >= 0.0, "volatility must be non negative")

  override def variance(t0: Double, x0: Double, dt: Double): Double = {
    if (Math.abs(speed) < Math.sqrt(1.0E-6)) vol * vol * dt
    else 0.5 * vol * vol / speed * (1.0 - Math.exp(-2.0 * speed * dt))
  }

  override def initialValue: Double = x0

  override def drift(t: Double, x: Double): Double = speed * (level - x)

  override def diffusion(t: Double, x: Double): Double = vol

  override def expectation(t0: Double, x0: Double, dt: Double): Double = {
    level + (x0 - level) * Math.exp(-speed * dt)
  }

  override def stdDeviation(t0: Double, x0: Double, dt: Double): Double =  Math.sqrt(variance(t0,x0,dt))

  override def evolve(t0: Double, x0: Double, dt: Double, dw: Double): Double = ???
}
