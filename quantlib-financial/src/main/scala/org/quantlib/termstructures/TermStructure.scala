package org.quantlib.termstructures

import org.quantlib.time.calendars.BusinessCalendar
import org.quantlib.time.daycounts.DayCountBasis
import org.quantlib.time.implicits.DateOps

/**
  * Created by neo on 19/03/2017.
  */

trait TermStructureT[T]
final case class TermStructure[D: DateOps](referenceDate: D,
                                           calendar:BusinessCalendar[D],
                                           dc: DayCountBasis[D]){
  def timeFromReferenceDate(to: D): Double = dc.yearFraction(referenceDate, to)
}

object TermStructure{

}