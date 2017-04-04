package org.quantlib.termstructures.inflation

import org.quantlib.termstructures.`yield`.YieldTermStructure
import org.quantlib.time.Period
import org.quantlib.time.enums.Frequency
import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 19/03/2017.
  */

final case class InflationTermStructure[D: DateOps](yts: YieldTermStructure[D],
                                                    observationLag: Period,
                                                    frequency: Frequency,
                                                    indexIsInterpolated: Boolean,
                                                    baseRate: Double,
                                                    seasonality: Seasonality)

final case class YoYInflationTermStructure[D](yts: YieldTermStructure[D],
                                              observationLag: Period,
                                              frequency: Frequency,
                                              indexIsInterpolated: Boolean,
                                              zeroRate: Double,
                                              seasonality: Seasonality)

final case class ZeroInflationTermStructure[D](yts: YieldTermStructure[D],
                                               observationLag: Period,
                                               frequency: Frequency,
                                               indexIsInterpolated: Boolean,
                                               baseRate: Double,
                                               seasonality: Seasonality)


