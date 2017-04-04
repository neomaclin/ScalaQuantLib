package org.quantlib.process

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4s.Implicits._

trait StochasticProcess[T] extends ((T, T) => T) {

  def dimensions: Int

  def factors: Int

  def initialValue: T

  def drift(t: Double, x: T): T

  def diffusion(t: Double, x: T): T

  def expectation(t0: Double, x0: T, dt: Double): T

  def stdDeviation(t0: Double, x0: T, dt: Double): T

  def evolve(t0: Double, x0: T, dt: Double, dw: T): T
}

trait StochasticProcess1D extends StochasticProcess[Double] {

  override def dimensions: Int = 1

  override def factors: Int = dimensions

  def variance(t0: Double, x0: Double, dt: Double): Double

  override def evolve(t0: Double, x0: Double, dt: Double, dw: Double): Double = {
    apply(expectation(t0, x0, dt), stdDeviation(t0, x0, dt) * dw)
  }

  override def apply(x0: Double, dx: Double): Double = x0 + dx
}

trait StochasticProcessND extends StochasticProcess[INDArray] {

  def covariance(t0: Double, x0: INDArray, dt: Double): INDArray

  override def evolve(t0: Double, x0: INDArray, dt: Double, dw: INDArray): INDArray = {
    apply(expectation(t0, x0, dt), stdDeviation(t0, x0, dt) * dw)
  }

  override def apply(x0: INDArray, dx: INDArray): INDArray = x0 + dx
}

object StochasticProcess1D {

  trait Discretization {

    def drift(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double

    def diffusion(process: StochasticProcess1D, t: Double, x: Double, dt: Double): Double

    def variance(process: StochasticProcess1D, t0: Double, x0: Double, dt: Double): Double

  }

}

object StochasticProcessND {

  trait Discretization {

    def drift(process: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray

    def diffusion(prrocess: StochasticProcessND, t: Double, x: INDArray, dt: Double): INDArray

    def covariance(prrocess: StochasticProcessND, t0: Double, x0: INDArray, dt: Double): INDArray
  }

}