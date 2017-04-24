//package org.quantlib.termstructures.`yield`
//
//import org.quantlib.InterestRate
//import org.quantlib.enums.Compounding
//import org.quantlib.quotes.Quote
//import org.quantlib.termstructures.{TermStructure, TermStructureT}
//import org.quantlib.time.daycounts.DayCountBasis
//import org.quantlib.time.enums.Frequency
//import org.quantlib.time.implicits.DateOps
//
///**
//  * Created by neo on 19/03/2017.
//  */
//
//
//
//object YieldTermStructure {
//  val dt = 0.0001
//}
//
////
////final case class ZeroSpreadedTermStructure[D: DateOps](ys: YieldTermStructure[D]
////
////                                                      )
//
//
//final case class YieldTermStructure[D: DateOps](termStructure: TermStructure[D],
//                                                jumps: List[(Quote, D)])
