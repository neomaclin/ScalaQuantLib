//package org.quantlib.instruments
//
//import org.quantlib.instruments.Exercise.Type.Bermudan
//import org.quantlib.time.calendars.BusinessCalendar
//import org.quantlib.time.enums.BusinessDayConvention
//import org.quantlib.time.implicits.DateOps
//import org.quantlib.time.implicits.DateOps._
//
//
//object Exercise {
//  val american = AmericanExercise
//  //val bermudan = BermudanExercise
//  //val europeanExercise = EuropeanExercise
//
//  sealed trait Type
//
//  object Type {
//
//    case object American extends Type
//
//    case object Bermudan extends Type
//
//    case object European extends Type
//
//  }
//
//}
//
//sealed trait ExerciseT
//
//final case class Exercise[D: DateOps](exerciseType: Exercise.Type, dates: List[D] = Nil) extends ExerciseT
//
//final case class EarlyExercise[D: DateOps](exercise: Exercise[D], payoffAtExpiry: Boolean = false) extends ExerciseT
//
//final case class RebatedExercise[D: DateOps](exercise: Exercise[D], rebates: List[Double],
//                                 rebateSettlementDays: Int,
//                                 rebatePaymentCalendar: BusinessCalendar[D],
//                                 rebatePaymentConvention: BusinessDayConvention) extends ExerciseT{
//  require(exercise.dates.length == rebates.length)
//  require(exercise.exerciseType == Bermudan)
//
//}
//
//object AmericanExercise {
//
//  def apply[D: DateOps](earliest: D, latest: D, payoffAtExpiry: Boolean = false): EarlyExercise[D] = {
//    require(earliest <= latest, "earliest > latest exercise date")
//
//    EarlyExercise(Exercise(Exercise.Type.American, List(earliest, latest)), payoffAtExpiry)
//  }
//
//  def apply[D: DateOps](latest: D, payoffAtExpiry: Boolean = false): EarlyExercise[D] = {
//    EarlyExercise[D](Exercise(Exercise.Type.American, List(DateOps.MIN, latest)), payoffAtExpiry)
//  }
//}
//
////object BermudanExercise {
////
////  def apply[D: DateOps](dates: List[D], payoffAtExpiry: Boolean = false): EarlyExercise[D] = {
////    require(dates.nonEmpty, "no exercise date given")
////    EarlyExercise[D](Exercise[D](Exercise.Type.Bermudan, dates.sorted), payoffAtExpiry)
////  }
////
////}
////
////object EuropeanExercise {
////  def apply[D: DateOps](date: D): Exercise[D] = Exercise(Exercise.Type.European, List(date))
////}
//
//
