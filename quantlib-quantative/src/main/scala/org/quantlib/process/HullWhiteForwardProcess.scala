//package org.quantlib.process
//
//
//import org.quantlib.math.Constants._
//
//final case class HullWhiteForwardProcess(yts: YieldTermStructure,
//                                         forwardMeasureTime: Double,
//                                         a: Double,
//                                         sigma: Double)
//  extends StochasticProcess1D with ForwardMeasure {
//  require(a >= 0.0, "negative a given")
//  require(sigma >= 0.0, "negative sigma given")
//
//  private val process = OrnsteinUhlenbeckProcess(a, sigma, yts.forwardRate(0.0, 0.0, Continuous, NoFrequency))
//
//  private def alpha(t: Double): Double = {
//    var alfa = if (a > QL_EPSILON) (sigma / a) * (1 - Math.exp(-a * t)) else sigma * t
//    alfa *= 0.5 * alfa
//    alfa += yts.forwardRate(t, t, Continuous, NoFrequency)
//
//    alfa
//  }
//
//  private def M_T(s: Double, t: Double, T: Double): Double = {
//    if (a > QL_EPSILON) {
//      val coeff = (sigma * sigma) / (a * a)
//      val exp1 = Math.exp(-a * (t - s))
//      val exp2 = Math.exp(-a * (T - t))
//      val exp3 = Math.exp(-a * (T + t - 2.0 * s))
//      coeff * (1 - exp1) - 0.5 * coeff * (exp2 - exp3)
//    } else {
//      val coeff = (sigma * sigma) / 2.0
//      coeff * (t - s) * (2.0 * T - t - s)
//    }
//  }
//
//  private def B(t: Double, T: Double): Double = {
//    if (a > QL_EPSILON) 1 / a * (1 - Math.exp(-a * (T - t))) else T - t
//  }
//
//  override def variance(t0: Double, x0: Double, dt: Double): Double = process.variance(t0, x0, dt)
//
//  override def initialValue: Double = process.x0
//
//  override def drift(t: Double, x: Double): Double = {
//
//    var alpha_drift = sigma * sigma / (2 * a) * (1 - Math.exp(-2 * a * t))
//    val shift = 0.0001
//    val f = yts.forwardRate(t, t, Continuous, NoFrequency)
//    val fup = yts.forwardRate(t + shift, t + shift, Continuous, NoFrequency)
//    val f_prime = (fup - f) / shift
//    alpha_drift += a * f + f_prime
//    process.drift(t, x) + alpha_drift - B(t, forwardMeasureTime) * sigma * sigma
//
//  }
//
//  override def diffusion(t: Double, x: Double): Double = process.diffusion(t, x)
//
//  override def expectation(t0: Double, x0: Double, dt: Double): Double = {
//
//    process.expectation(t0, x0, dt) + alpha(t0 + dt) - alpha(t0) * Math.exp(-a * dt) - M_T(t0, t0 + dt, forwardMeasureTime)
//
//  }
//
//  override def stdDeviation(t0: Double, x0: Double, dt: Double): Double = process.stdDeviation(t0, x0, dt)
//
//
//}
