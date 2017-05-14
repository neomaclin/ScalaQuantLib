package org.quantlib.currencies


import java.time.LocalDate

import breeze.numerics.round
import org.quantlib.currencies.Money.ConversionType.{BaseCurrencyConversion, NoConversion}
import org.scalatest._

import scala.collection.concurrent.TrieMap

/**
  * Created by neo on 15/04/2017.
  */
class MoneySuite extends FunSuite {
  test("Testing money arithmetic without conversions....") {

    implicit val conversionType = NoConversion
    implicit val exchangeRateManager = ExchangeRateManager.defaultExchangeRateManager
    implicit val date = LocalDate.now
    implicit val exchangeType: ExchangeRate.Type = ExchangeRate.Type.Direct

    val m1 = Money(50000.0, Europe.EUR)
    val m2 = Money(100000.0, Europe.EUR)
    val m3 = Money(500000.0, Europe.EUR)

    val calculated: Money = (m1 * 3.0) + (2.5 * m2) - (m3 / 5.0)
    val x = m1.value * 3.0 + 2.5 * m2.value - m3.value / 5.0
    val expected = Money(x, Europe.EUR)

    assert(calculated === expected)

  }

//  test("Testing money arithmetic with conversion to base currency") {
//
//
//    val m1 = Money(50000.0, Europe.GBP)
//    val m2 = Money(100000.0, Europe.EUR)
//    val m3 = Money(500000.0, America.USD)
//
//    val eurusd = ExchangeRate(Europe.EUR, America.USD, 1.2042)
//    val eurgbp = ExchangeRate(Europe.EUR, Europe.GBP, 0.6612)
//
//    import org.quantlib.time.implicits.DateOps
//    import org.quantlib.time.implicits.DateOps._
//    import org.quantlib.time.implicits.Date._
//
//    implicit val exchangeRateManager = ExchangeRateManager[LocalDate](
//      TrieMap(
//        Key(Europe.EUR, America.USD) -> Entry[LocalDate](eurusd, DateOps.MIN, DateOps.MAX),
//        Key(Europe.EUR, Europe.GBP) -> Entry[LocalDate](eurgbp, DateOps.MIN, DateOps.MAX)
//      )
//    )
//
//    implicit val conversionType = BaseCurrencyConversion(Europe.EUR)
//    implicit val date = LocalDate.now
//    implicit val exchangeType: ExchangeType = ExchangeType.Direct
//
//
//    val calculated = (m1 * 3.0) + (2.5 * m2) - (m3 / 5.0)
//
//    val round = Europe.EUR.rounding
//    val x = round(m1.value * 3.0 / eurgbp.rate) + 2.5 * m2.value - round(m3.value / (5.0 * eurusd.rate))
//    val expected = Money(x, Europe.EUR)
//
//
//    //assert(calculated === expected)
//
//  }
}
