package org.quantlib.process


import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4s.Implicits._
/**
  * Created by neo on 14/03/2017.
  */
final case class G2Process(a: Double, sigma: Double,
                           b: Double, eta: Double, rho: Double) extends StochasticProcessND {
  private val xProcess = OrnsteinUhlenbeckProcess(a, sigma)
  private val yProcess = OrnsteinUhlenbeckProcess(b, eta)

  private def checkSize(x: INDArray): Unit = require(x.length == 2)

  override def covariance(t0: Double, x0: INDArray, dt: Double): INDArray = {
    val sigma = stdDeviation(t0, x0, dt)
    sigma * sigma.T
  }

  override def dimensions: Int = 2

  override def factors: Int = 2

  override def initialValue: INDArray = Seq(0.0, 0.0).toNDArray

  override def drift(t: Double, x: INDArray): INDArray = {
    checkSize(x)
    Seq(xProcess.drift(t, x(0)), yProcess.drift(t, x(1))).toNDArray
  }

  override def diffusion(t: Double, x: INDArray): INDArray = {
    Seq(
      Seq(sigma, 0.0),
      Seq(rho * sigma, Math.sqrt(1.0 - rho * rho) * eta)
    ).toNDArray
  }

  override def expectation(t0: Double, x0: INDArray, dt: Double): INDArray = {
    checkSize(x0)
    Seq(
      xProcess.expectation(t0, x0(0), dt),
      yProcess.expectation(t0, x0(1), dt)
    ).toNDArray
  }

  override def stdDeviation(t0: Double, x0: INDArray, dt: Double): INDArray = {
    checkSize(x0)

    val sigma1 = xProcess.stdDeviation(t0, x0(0), dt)
    val sigma2 = yProcess.stdDeviation(t0, x0(1), dt)
    val expa = Math.exp(-a * dt)
    val expb = Math.exp(-b * dt)
    val H = (rho * sigma * eta) / (a + b) * (1 - expa * expb)
    val den = (0.5 * sigma * eta) * Math.sqrt((1 - expa * expa) * (1 - expb * expb) / (a * b))
    val newRho = H / den
    Seq(
      Seq(sigma1, 0.0),
      Seq(newRho * sigma2, Math.sqrt(1.0 - newRho * newRho) * sigma2)
    ).toNDArray
  }

}


final case class G2ForwardProcess(a: Double, sigma: Double,
                                  b: Double, eta: Double, rho: Double,
                                  forwardMeasureTime: Double) extends StochasticProcessND with ForwardMeasure {
  private val xProcess = OrnsteinUhlenbeckProcess(a, sigma)
  private val yProcess = OrnsteinUhlenbeckProcess(b, eta)

  private def checkSize(x: INDArray): Unit = require(x.length == 2)

  def xForwardDrift(t: Double, T: Double): Double = {

    val expatT = Math.exp(-a * (T - t))
    val expbtT = Math.exp(-b * (T - t))

    -(sigma * sigma / a) * (1 - expatT) - (rho * sigma * eta / b) * (1 - expbtT)
  }

  def yForwardDrift(t: Double, T: Double): Double = {

    val expatT = Math.exp(-a * (T - t))
    val expbtT = Math.exp(-b * (T - t))

    -(eta * eta / b) * (1 - expbtT) - (rho * sigma * eta / a) * (1 - expatT)
  }

  def Mx_T(s: Double, t: Double, T: Double): Double = {

    var M = ((sigma * sigma) / (a * a) + (rho * sigma * eta) / (a * b)) * (1 - Math.exp(-a * (t - s)))
    M += -(sigma * sigma) / (2 * a * a) * (Math.exp(-a * (T - t)) - Math.exp(-a * (T + t - 2 * s)))
    M += -(rho * sigma * eta) / (b * (a + b)) * (Math.exp(-b * (T - t)) - Math.exp(-b * T - a * t + (a + b) * s))

    M
  }

  def My_T(s: Double, t: Double, T: Double): Double = {
    var M = ((eta * eta) / (b * b) + (rho * sigma * eta) / (a * b)) * (1 - Math.exp(-b * (t - s)))
    M += -(eta * eta) / (2 * b * b) * (Math.exp(-b * (T - t)) - Math.exp(-b * (T + t - 2 * s)))
    M += -(rho * sigma * eta) / (a * (a + b)) * (Math.exp(-a * (T - t)) - Math.exp(-a * T - b * t + (a + b) * s))

    M
  }

  override def covariance(t0: Double, x0: INDArray, dt: Double): INDArray = {
    val sigma = stdDeviation(t0, x0, dt)
    sigma * sigma.T
  }

  override def dimensions: Int = 2

  override def factors: Int = 2

  override def initialValue: INDArray = List(0.0, 0.0).toNDArray

  override def drift(t: Double, x: INDArray): INDArray = {
    checkSize(x)
    Seq(
      xProcess.drift(t, x(0)) + xForwardDrift(t, forwardMeasureTime),
      yProcess.drift(t, x(1)) + yForwardDrift(t, forwardMeasureTime)
    ).toNDArray
  }

  override def diffusion(t: Double, x: INDArray): INDArray = {
    Seq(
      Seq(sigma, 0.0),
      Seq(rho * sigma, Math.sqrt(1.0 - rho * rho) * eta)
    ).toNDArray
  }

  override def expectation(t0: Double, x0: INDArray, dt: Double): INDArray = {
    checkSize(x0)
    Seq(
      xProcess.expectation(t0, x0(0), dt) - Mx_T(t0, t0 + dt, forwardMeasureTime),
      yProcess.expectation(t0, x0(1), dt) - My_T(t0, t0 + dt, forwardMeasureTime)
    ).toNDArray
  }

  override def stdDeviation(t0: Double, x0: INDArray, dt: Double): INDArray = {
    checkSize(x0)

    val sigma1 = xProcess.stdDeviation(t0, x0(0), dt)
    val sigma2 = yProcess.stdDeviation(t0, x0(1), dt)
    val expa = Math.exp(-a * dt)
    val expb = Math.exp(-b * dt)
    val H = (rho * sigma * eta) / (a + b) * (1 - expa * expb)
    val den = (0.5 * sigma * eta) * Math.sqrt((1 - expa * expa) * (1 - expb * expb) / (a * b))
    val newRho = H / den
    Seq(
      Seq(sigma1, 0.0),
      Seq(newRho * sigma2, Math.sqrt(1.0 - newRho * newRho) * sigma2)
    ).toNDArray
  }

}