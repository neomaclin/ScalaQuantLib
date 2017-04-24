//package org.quantlib
//
//import java.time.LocalDate
//
//import org.quantlib.instruments.{ExerciseT, StrikedPayoff}
//import org.quantlib.instruments.options.Option.Average
//
///**
//  * Created by neo on 24/03/2017.
//  */
//package object options {
//
//  final case class ContinuousAveragingAsianOption(averageType: Average.Type,
//                                                  payoff: StrikedPayoff,
//                                                  exercise: ExerciseT)
//
//  final case class DiscreteAveragingAsianOption(averageType: Average.Type,
//                                                runningAccumulator: Double,
//                                                pastFixings: Int,
//                                                fixingDates: LocalDate,
//                                                payoff: StrikedPayoff,
//                                                exercise: ExerciseT)
//
//}
