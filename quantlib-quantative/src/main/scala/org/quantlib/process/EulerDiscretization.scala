package org.quantlib.process
import org.nd4j.linalg.api.ndarray.INDArray

/**
  * Created by neo on 15/03/2017.
  */
object EulerDiscretization extends StochasticProcess1D.Discretization with StochasticProcessND.Discretization {
  override def drift(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double = ???

  override def diffusion(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double = ???

  override def variance(process: StochasticProcess1D, t0: Double, x0: Double, dt: Double): Double = ???

  override def drift(process: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray = ???

  override def diffusion(prrocess: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray = ???

  override def covariance(prrocess: StochasticProcessND, t0: Double, x0: INDArray, dt: Double): INDArray = ???
}

object EndEulerDiscretization extends StochasticProcess1D.Discretization with StochasticProcessND.Discretization {
  override def drift(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double = ???

  override def diffusion(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double = ???

  override def variance(process: StochasticProcess1D, t0: Double, x0: Double, dt: Double): Double = ???

  override def drift(process: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray = ???

  override def diffusion(prrocess: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray = ???

  override def covariance(prrocess: StochasticProcessND, t0: Double, x0: INDArray, dt: Double): INDArray = ???
}