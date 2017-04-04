package org.quantlib.instruments.options.oneasset

import java.time.LocalDate

import org.quantlib.instruments._
import org.quantlib.instruments.options.Option.Average

/**
  * Created by neo on 02/04/2017.
  */
object AsianOption {

  object ContinuousAveraging{
    final case class Request(averageType: Average.Type)
  }
  object DiscreteAveraging{
    final case class Request(averageType: Average.Type,
                             runningAccumulator: Double,
                             pastFixings: Double)
  }

}

final case class ContinuousAveragingAsianOption(averageType: Average.Type,
                                                payoff: StrikedPayoff,
                                                exercise: Exercise[LocalDate])
final case class DiscreteAveragingAsianOption(averageType: Average.Type,
                                              runningAccumulator: Double,
                                              pastFixings: Double,
                                              fixingDates: Seq[LocalDate],
                                              payoff: StrikedPayoff,
                                              exercise: Exercise[LocalDate])
