package org.quantlib


/**
  * Created by neo on 26/02/2017.
  */
package object process {



  trait ForwardMeasure {
    def forwardMeasureTime: Double
  }
}
