//package org.quantlib.instruments
//
//import scala.math.max
//
///**
//  * Created by neo on 20/03/2017.
//  */
//trait Payoff extends (Double => Double) {
//  def name: String
//  def description: String
//}
//
//sealed abstract class TypedPayoff(optionType: options.Option.Type) extends Payoff {
//
//  def description: String = name + " " + optionType
//
//}
//
//final case class FloatingTypePayoff(optionType: options.Option.Type) extends TypedPayoff(optionType){
//  val name: String = "FloatingType"
//}
//
//sealed abstract class StrikedPayoff(val name: String, optionType: options.Option.Type) extends TypedPayoff(optionType) {
//  def strike: Double
//
//  override def description = s"${super.description}, $strike strike"
//}
//
//final case class PlainVanillaPayoff(optionType: options.Option.Type,
//                                    strike: Double) extends StrikedPayoff("Vanilla", optionType) {
//  def apply(price: Double): Double = optionType match {
//    case options.Option.Type.Call => max(price - strike, 0.0)
//    case options.Option.Type.Put => max(strike - price, 0.0)
//  }
//}
//
//final case class PercentageStrikePayoff(optionType: options.Option.Type,
//                                        strike: Double) extends StrikedPayoff("PercentageStrike", optionType) {
//
//  def apply(price: Double): Double = optionType match {
//    case options.Option.Type.Call => max(1.0 - strike, 0.0)
//    case options.Option.Type.Put => max(strike - 1.0, 0.0)
//  }
//}
//
//final case class AssetOrNothingPayoff(optionType: options.Option.Type,
//                                      strike: Double) extends StrikedPayoff("AssetOrNothing", optionType) {
//  def apply(price: Double): Double = optionType match {
//    case options.Option.Type.Call => if (price - strike > 0.0) price else 0.0
//    case options.Option.Type.Put => if (strike - price > 0.0) price else 0.0
//  }
//}
//
//final case class CashOrNothingPayoff(optionType: options.Option.Type,
//                                     strike: Double,
//                                     cashPayoff: Double) extends StrikedPayoff("CashOrNothing", optionType) {
//  override def description = s"${super.description}, $cashPayoff cash payoff "
//
//  def apply(price: Double): Double = optionType match {
//    case options.Option.Type.Call => if (price - strike > 0.0) cashPayoff else 0.0
//    case options.Option.Type.Put => if (strike - price > 0.0) cashPayoff else 0.0
//  }
//}
//
//final case class GapPayoff(optionType: options.Option.Type,
//                           strike: Double,
//                           secondStrike: Double) extends StrikedPayoff("Gap", optionType) {
//  override def description = s"${super.description}, $secondStrike second strike payoff "
//
//  def apply(price: Double): Double = optionType match {
//    case options.Option.Type.Call => if (price - strike >= 0.0) price - secondStrike else 0.0
//    case options.Option.Type.Put => if (strike - price >= 0.0) secondStrike - price else 0.0
//  }
//}
//
//final case class SuperFundPayoff(strike: Double,
//                                 secondStrike: Double) extends StrikedPayoff("SuperFund", options.Option.Type.Call) {
//  require(strike > 0.0, s"strike ($strike) must be positive")
//  require(secondStrike > strike, s"second strike ($secondStrike) must be higher than first strike ($strike)")
//
//  def apply(price: Double): Double = if (price >= strike && price < secondStrike) price / strike else 0.0
//}
//
//final case class SuperSharePayoff(strike: Double,
//                                  secondStrike: Double,
//                                  cashPayoff: Double) extends StrikedPayoff("SuperShare", options.Option.Type.Call) {
//  require(secondStrike > strike, s"second strike ($secondStrike) must be higher than first strike ($strike)")
//
//  override def description = s"${super.description}, $secondStrike second strike payoff, $cashPayoff amount"
//
//  def apply(price: Double): Double = if (price >= strike && price < secondStrike) cashPayoff else 0.0
//}
