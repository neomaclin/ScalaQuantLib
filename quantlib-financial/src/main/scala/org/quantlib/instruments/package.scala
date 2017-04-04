package org.quantlib

/**
  * Created by neo on 27/03/2017.
  */
package object instruments {
  implicit class Instrument[T: InstrumentT](val in: T) extends  Serializable{
      val implicitVal = implicitly[InstrumentT[T]]
  }
}
