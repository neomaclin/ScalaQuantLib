package org.quantlib.currencies


import java.time.{Year}

import org.quantlib.currencies.ExchangeType._
import org.quantlib.time.implicits.DateOps
import org.quantlib.time.implicits.DateOps._

import scala.collection.concurrent.TrieMap

final case class Entry[D : DateOps](rate: ExchangeRate, start: D, end: D){
  def isValidAt(date: D): Boolean = (date >= start) && (date <= end)
}


final case class Key(c1: Currency, c2: Currency)

final case class ExchangeRateManager[D: DateOps](entries: TrieMap[Key, Entry[D]]) {

  def add(entry: Entry[D]): ExchangeRateManager[D] = {

    copy(entries += (Key(entry.rate.source, entry.rate.target) -> entry))
  }

  private def directLookup(source: Currency, target: Currency, date: D): Option[ExchangeRate] = {
    entries.get(Key(source, target)).filter(_.isValidAt(date)).map(_.rate)
  }

  private def smartLookUp(source: Currency, target: Currency, date: D): Option[ExchangeRate] = {
    directLookup(source, target, date) match {
      case None =>
        val left = entries.keys.filter(key => (key.c1 == source && key.c2 != target) || (key.c2 == source && key.c1 != target))
        val right = entries.keys.filter(key => (key.c1 == target && key.c2 != source) || (key.c2 == target && key.c1 != source))

        (left zip right).filter { case (key1, key2) =>
          if (key1.c1 == source) key1.c2 == key2.c1 || key1.c2 == key2.c2 else key1.c1 == key2.c1 || key1.c1 == key2.c2
        }.collectFirst {
          case (key1, key2) => entries(key1).rate.chain(entries(key2).rate)
        }.flatten
      case attempt => attempt
    }

  }

  def lookup(source: Currency, target: Currency, date: D, exchangeType: ExchangeType): Option[ExchangeRate] = {
    if (source == target) Some(ExchangeRate(source, target, 1.0))
    else exchangeType match {
      case Direct => directLookup(source, target, date)
      case Derived(_, _) =>
        (source.triangulationCurrency, target.triangulationCurrency) match {
          case (Some(link), None) =>
            if (link == target) {
              directLookup(source, link, date)
            } else {
              val result = for {
                direct <- directLookup(source, link, date)
                next <- lookup(link, target, date, exchangeType)
              } yield {
                direct.chain(next)
              }
              result.flatten
            }
          case (None, Some(link)) =>
            if (link == source) {
              directLookup(link, target, date)
            } else {
              val result = for {
                next <- lookup(source, link, date, exchangeType)
                direct <- directLookup(link, target, date)
              } yield {
                next.chain(direct)
              }
              result.flatten

            }
          case _ => smartLookUp(source, target, date)
        }
    }
  }

}

object ExchangeRateManager {

  import org.quantlib.time.implicits.Date._

  val defaultExchangeRateManager = ExchangeRateManager(knownRates)
  import java.time.Month._
  import org.quantlib.currencies.Europe._
  private val knownRates = TrieMap(
    Key(EUR, ATS) -> Entry(ExchangeRate(EUR, ATS, 13.7603), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, BEF) -> Entry(ExchangeRate(EUR, BEF, 40.3399), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, DEM) -> Entry(ExchangeRate(EUR, DEM, 1.95583), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, ESP) -> Entry(ExchangeRate(EUR, ESP, 166.386), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, FIM) -> Entry(ExchangeRate(EUR, FIM, 5.94573), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, FRF) -> Entry(ExchangeRate(EUR, FRF, 6.55957), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, GRD) -> Entry(ExchangeRate(EUR, GRD, 340.750), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, IEP) -> Entry(ExchangeRate(EUR, IEP, 0.787564), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, ITL) -> Entry(ExchangeRate(EUR, ITL, 1936.27), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, LUF) -> Entry(ExchangeRate(EUR, LUF, 40.3399), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, NLG) -> Entry(ExchangeRate(EUR, NLG, 2.20371), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(EUR, PTE) -> Entry(ExchangeRate(EUR, PTE, 200.482), DateOps.from(1, JANUARY, Year.of(1999)), DateOps.MAX),
    Key(TRY, TRL) -> Entry(ExchangeRate(TRY, TRL, 1000000.0), DateOps.from(1,JANUARY, Year.of(2005)), DateOps.MAX),
    Key(RON, ROL) -> Entry(ExchangeRate(RON, ROL, 10000.0), DateOps.from(1, JULY, Year.of(2005)), DateOps.MAX)
  )
}