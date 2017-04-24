//package org.quantlib
//
//import java.time.LocalDate
//
//import cats.{Functor, Monad}
//
///**
//  * Created by neo on 15/03/2017.
//  */
//
//package object quotes {
//
//
//  sealed trait Quote {
//
//    def value: Double
//
//    def isValid: Boolean
//
//    def flatMap(f: Double => Quote): Quote = if (isValid) f(value) else InvalidQuote
//
//  }
//
//  trait ValidQuote extends Quote {
//    final protected val isValid = true
//  }
//
//  case object InvalidQuote extends Quote {
//
//    final protected val isValid = false
//    final val value = Double.NaN
//    def map(f: Double => Double): Quote = this
//
//  }
//  final case class SimpleQuote(value: Double) extends ValidQuote
//
//  final case class DerivedQuote(f: Double => Double, value: Double) extends ValidQuote
//
////  final case class LastFixingQuote(index: Index, referenceDate: LocalDate, value: Double) extends ValidQuote
//
//  final case class CompositedQuote(quote1: Quote, quote2: Quote, value: Double) extends ValidQuote
//
//  final case class EurodollarFuturesImpliedStdDevQuote(forward: Quote, callOrPut: Either[Quote, Quote],
//                                                       strike: Double, guess: Double, value: Double) extends ValidQuote
//
//  final case class ForwardSwapQuote(valueDate: LocalDate, startDate: LocalDate, fixingDate: LocalDate, value: Double) extends ValidQuote
//
//  final case class ForwardValueQuote(value: Double) extends ValidQuote
//
//
//  final case class ImpliedStdDevQuote(forward: Quote, price: Quote, optionType: Option.Type,
//                                      strike: Double, guess: Double, value: Double) extends ValidQuote
//
//  final case class FuturesConvAdjustmentQuote(futuresValue: Double,
//                                              volatility: Double,
//                                              meanReversion: Double,
//                                              immDate: LocalDate, value: Double) extends ValidQuote
//
//
//  def compose(element1: Quote, element2: Quote, f: (Double, Double) => Double): Quote = {
//    for {
//      value1 <- element1
//      value2 <- element2
//    } yield CompositedQuote(element1, element2, f(value1, value2))
//  }
//
//  def derive(element1: Quote, f: Double => Double): Quote = {
//    for {
//      value1 <- element1
//    } yield DerivedQuote(f, f(value1))
//  }
//
//
//  def lastFixing(index: Index)(implicit enviromentSetting: Settings): Quote = {
//    if (index.timeSeries.isEmpty) {
//      InvalidQuote
//    } else {
//      val referenceDate = DateTimeHelper.min(index.timeSeries.lastDate, enviromentSetting.evaluationDate)
//      val value = index.fixing(referenceDate)
//      LastFixingQuote(index, referenceDate, value)
//    }
//  }
//
//  def eurodollarFuturesImpliedStdDev(forward: Quote, callOrPut: Either[Quote, Quote],
//                                     strike: Double, guess: Double, accuracy: Double,
//                                     maxIteration: Int): Quote = {
//    val localStrike = 100.0 - strike
//    val discount = 1.0
//    val displacement = 0.0
//
//    for {
//      forwardValue <- forward
//      val localForwardValue = 100.0 - forwardValue
//      val priceQuote = if (localStrike > localForwardValue) callOrPut.left else callOrPut.right
//      priceValue <- priceQuote
//    } yield {
//      val impliedStdev = if (localStrike > localForwardValue) {
//        blackFormulaImpliedStdDev(Option.Call, localStrike,
//          localForwardValue, priceValue,
//          discount, displacement,
//          guess, accuracy, maxIteration)
//      } else {
//        blackFormulaImpliedStdDev(Option.Put, localStrike,
//          localForwardValue, priceValue,
//          discount, displacement,
//          guess, accuracy, maxIteration)
//      }
//      EurodollarFuturesImpliedStdDevQuote(forward, callOrPut, strike, guess, impliedStdev)
//    }
//
//  }
//
//
//  def impliedStdDev(forward: Quote, price: Quote, optionType: Option.Type,
//                    strike: Double, guess: Double, accuracy: Double, maxIteration: Int): Quote = {
//    val discount = 1.0
//    val displacement = 0.0
//
//    for {
//      forwardValue <- forward
//      priceValue <- price
//    } yield {
//      val impliedStdev = blackFormulaImpliedStdDev(optionType, strike,
//        forwardValue, priceValue,
//        discount, displacement,
//        guess, accuracy, maxIteration)
//
//      ImpliedStdDevQuote(forward, price, optionType, strike, guess, impliedStdev)
//    }
//
//  }
//
//  def forwardValue(index: Index, fixingDate: LocalDate): Quote = {
//    ForwardValueQuote(index.fixing(fixingDate))
//  }
//
//  val basisPoint = 1.0e-4
//
//  def forwardSwap(swapIndex: SwapIndex, spread: Quote, fwdStart: Period)(implicit enviromentSetting: Settings): Quote = {
//    val evaluationDate = enviromentSetting.evaluationDate
//    val valueDate = swapIndex.fixingCalendar.advance(evaluationDate, swapIndex.fixingDays, Timeunit.Days, Following)
//    val startDate = swapIndex.fixingCalendar.advance(valueDate, fwdStart.length, fwdStart.units, Following)
//    val fixingDate = swapIndex.fixingDate(startDate)
//    val swap = swapIndex.underlyingSwap(fixingDate)
//    val floatingLegNPV = swap.floatingLegNPV
//
//    for {
//      value <- spread
//    } yield {
//      val spreadNPV = swap.floatingLegBPS / basisPoint * value
//      val totNPV = -(floatingLegNPV + spreadNPV)
//      ForwardSwapQuote(valueDate, startDate, fixingDate, totNPV / (swap.fixedLegBPS / basisPoint))
//    }
//  }
//
//  def futuresConvAdjustmentQuote(index: IborIndex, futuresDate: LocalDate,
//                                 futuresQuote: Quote, volatility: Quote, meanReversion: Quote)(implicit enviromentSetting: Settings): Quote = {
//    val indexMaturityDate = index.maturityDate(futuresDate)
//    val settlementDate = enviromentSetting.evaluationDate
//    val dc = index.dayCounter
//    val startTime = dc.yearFraction(settlementDate, futuresDate)
//    val indexMaturity = dc.yearFraction(settlementDate, indexMaturityDate)
//    for {
//      futuresValue <- futuresQuote
//      vol <- volatility
//      mean <- meanReversion
//    } yield {
//
//      val value = HullWhite.convexityBias(futuresValue, startTime, indexMaturity, vol, mean)
//
//      FuturesConvAdjustmentQuote(futuresValue, vol, mean, futuresDate, value)
//    }
//  }
//
//}