package org

import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 16/03/2017.
  */
package object quantlib {
  type TimeSeries[D, T] = Map[DateOps[D],T]
  type Spread = Double
  type Time = Double
}
