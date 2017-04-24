//package org.quantlib.indexes
//
//import java.time.LocalDate
//
//import org.quantlib.currencies.Currency
//import org.quantlib.termstructures.`yield`.YieldTermStructure
//import org.quantlib.time.{Period, Schedule}
//
///**
//  * Created by neo on 02/04/2017.
//  */
//object IborIndex {
//  trait Family
//
//}
//
//final case class IborIndex[T](family: IborIndex.Family,
//                           currency: Currency,
//                           tenor : Period,
//                           schedule: Schedule[LocalDate],
//                           ys: YieldTermStructure[T])
//
//final case class OvernightIndex()