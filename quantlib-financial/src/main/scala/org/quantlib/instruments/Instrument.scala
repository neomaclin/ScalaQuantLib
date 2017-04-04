package org.quantlib.instruments

/**
  * Created by neo on 19/03/2017.
  */
trait InstrumentT[I] {
  def NPV: Double
  def errorEstimate: Double
  def valuationDate: Double
  def isExpired: Boolean
}


