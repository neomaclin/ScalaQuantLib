package org.quantlib.termstructures.volatility

import org.quantlib.termstructures.TermStructure
import org.quantlib.time.enums.BusinessDayConvention
import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 19/03/2017.
  */
final case class VolatilityTermStructure[D: DateOps](termStructure: TermStructure[D],
                                                     bdc: BusinessDayConvention)

object VolatilityTermStructure{}

