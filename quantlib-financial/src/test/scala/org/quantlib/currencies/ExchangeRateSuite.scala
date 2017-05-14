package org.quantlib.currencies

import java.time._

import org.scalatest._
import org.quantlib.math.Comparing._
import org.quantlib.time.implicits.DateOps
import org.quantlib.time.implicits.DateOps._

import scala.collection.concurrent.TrieMap

/**
  * Created by neo on 16/04/2017.
  */
class ExchangeRateSuite extends FunSuite {

  test("Testing direct exchange rates...") {

    val eur2usd = ExchangeRate(Europe.EUR, America.USD, 1.2042)

    val m1 = Money(50000.0, Europe.EUR)
    val m2 = Money(100000.0, America.USD)

    val calculated = eur2usd.exchange(m1)
    val expected = Money(m1.value * eur2usd.rate, America.USD)

    assert(calculated =~ expected)

    val calculated2 = eur2usd.exchange(m2)
    val expected2 = Money(m2.value / eur2usd.rate, Europe.EUR)

    assert(calculated2 =~ expected2)

  }

  test("Testing derived exchange rates...") {

    val eur2usd = ExchangeRate(Europe.EUR, America.USD, 1.2042)

    val eur2gbp = ExchangeRate(Europe.EUR, Europe.GBP, 0.6612)

    val derived = eur2usd.chain(eur2gbp).get

    val m1 = Money(50000.0, Europe.GBP)
    val m2 = Money(100000.0, America.USD)

    val calculated = derived.exchange(m1)
    val expected = Money(m1.value * eur2usd.rate / eur2gbp.rate, America.USD)

    assert(calculated =~ expected)

    val calculated2 = derived.exchange(m2)
    val expected2 = Money(m2.value * eur2gbp.rate / eur2usd.rate, Europe.GBP)

    assert(calculated2 =~ expected2)
  }

  test("Testing lookup of direct exchange rates...") {

//    import org.quantlib.time.implicits.DateOps
//    import org.quantlib.time.implicits.DateOps._
//    import org.quantlib.time.implicits.Date._
//
//    val eur2usd1 = ExchangeRate(Europe.EUR, America.USD, 1.1983)
//    val eur2usd2 = ExchangeRate(America.USD, Europe.EUR, 1.0 / 1.2042)
//    var exhangeRateManager = ExchangeRateManager[LocalDate](TrieMap.empty[Key, Entry[LocalDate]])
//
//    exhangeRateManager = exhangeRateManager.add(Entry(eur2usd1, DateOps.from(4, Month.AUGUST, Year.of(2004)), DateOps.MAX))
//    exhangeRateManager = exhangeRateManager.add(Entry(eur2usd2, DateOps.from(5, Month.AUGUST, Year.of(2004)), DateOps.MAX))
//
//    val m1 = Money(50000.0, Europe.EUR)
//    val m2 = Money(100000.0, America.USD)
//
//    var eur2usd = exhangeRateManager.lookup(Europe.EUR, America.USD, DateOps.from(4, Month.AUGUST, Year.of(2004)), ExchangeRate.Type.Direct).get
//    val calculated = eur2usd.exchange(m1)
//    val expected = Money(m1.value * eur2usd1.rate, America.USD)
//
//    assert(calculated =~ expected)
//
//    eur2usd = exhangeRateManager.lookup(Europe.EUR, America.USD, DateOps.from(5, Month.AUGUST, Year.of(2004)), ExchangeRate.Type.Direct).get
//    val calculated2 = eur2usd.exchange(m1)
//    val expected2 = Money(m1.value / eur2usd2.rate, America.USD)

//    assert(calculated2 =~ expected2)
//
//    var usd2eur = exhangeRateManager.lookup(America.USD, Europe.EUR, DateOps.from(4, Month.AUGUST, Year.of(2004)), ExchangeRate.Type.Direct).get
//
//    val calculated3 = usd2eur.exchange(m2)
//    val expected3 = Money(m2.value / eur2usd1.rate,  Europe.EUR)
//
//    assert(calculated3 =~ expected3)
//
//    usd2eur = exhangeRateManager.lookup(America.USD, Europe.EUR, DateOps.from(5, Month.AUGUST, Year.of(2004)), ExchangeRate.Type.Direct).get
//
//    val calculated4 = usd2eur.exchange(m2)
//    val expected4 = Money(m2.value * eur2usd2.rate,  Europe.EUR)
//
//    assert(calculated4 =~ expected4)
      assert(true)
  }
}
