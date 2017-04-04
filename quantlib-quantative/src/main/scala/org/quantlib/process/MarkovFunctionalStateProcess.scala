package org.quantlib.process
import java.lang.Math._

/**
  * Created by neo on 25/02/2017.
  */
final case class MarkovFunctionalStateProcess(reversion: Double,
                                              times: Seq[Double],
                                              vols: Seq[Double]) extends StochasticProcess1D {

  require(times.size == vols.size - 1,
    s"number of volatilities (${vols.size}) compared to number of times (${times.size}) must be bigger by one")

  require(times.sorted == times, "times must be in increasing order ")

  require(vols.forall(_ >= 0.0), "volatilities must be non negative")

  private val reversionZero = (reversion < 1e-10) && (-reversion < 1e-10)

  private def findTimesIndex(t: Double): Int = {
    val pos = times.indexWhere(_ > t)
    if (pos > -1) pos else times.length
  }

  override def variance(t: Double, x0: Double, dt: Double): Double = {
    if (dt < 1e-10) {
      0.0
    } else {
      if (times.isEmpty) {
        if (reversionZero) dt
        else 1.0 / (2.0 * reversion) * (exp(2.0 * reversion * (t + dt)) - exp(2.0 * reversion * t))

      } else {
        val i = findTimesIndex(t)
        val j = findTimesIndex(t + dt)

        var v = 0.0
        i until j foreach { k =>
          val vol = max(if (k > 0) times(k - 1) else 0.0, t)
          if (reversionZero)
            v += vols(k) * vols(k) * (times(k) - vol)
          else
            v += 1.0 / (2.0 * reversion) * vols(k) * vols(k) * (exp(2.0 * reversion * times(k)) - exp(2.0 * reversion * vol))
        }

        if (reversionZero)
          v += vols(j) * vols(j) * (t + dt - max(if (j > 0) times(j - 1) else 0.0, t))
        else
          v += 1.0 / (2.0 * reversion) * vols(j) * vols(j) * (exp(2.0 * reversion * (t + dt)) - exp(2.0 * reversion * max(if (j > 0) times(j - 1) else 0.0, t)))

        v

      }
    }
  }

  override def initialValue: Double =  0.0

  override def drift(t: Double, x: Double): Double =  0.0

  override def diffusion(t: Double, x: Double): Double =  vols(findTimesIndex(t))

  override def expectation(t: Double, x0: Double, dt: Double): Double = 0.0

  override def stdDeviation(t: Double, x0: Double, dt: Double): Double = sqrt(variance(t, x0, dt));


}