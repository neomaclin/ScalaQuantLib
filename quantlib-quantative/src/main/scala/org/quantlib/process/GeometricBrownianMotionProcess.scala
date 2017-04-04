package org.quantlib.process

/**
  * Created by neo on 26/02/2017.
  */
final case class GeometricBrownianMotionProcess(initialValue: Double,
                                                mue: Double,
                                                sigma: Double,
                                                private val discretization: StochasticProcess1D.Discretization = EulerDiscretization )
extends StochasticProcess1D {


  override def variance(t0: Double, x0: Double, dt: Double): Double = {
    discretization.variance(this,t0,x0,dt)
  }

  override def drift(t: Double, x: Double): Double = mue * x

  override def diffusion(t: Double, x: Double): Double = sigma * x

  override def expectation(t0: Double, x0: Double, dt: Double): Double = {
    this.apply(x0,discretization.drift(this,t0,x0,dt))
  }

  override def stdDeviation(t0: Double, x0: Double, dt: Double): Double = {
    discretization.diffusion(this,t0,x0,dt)
  }


}
