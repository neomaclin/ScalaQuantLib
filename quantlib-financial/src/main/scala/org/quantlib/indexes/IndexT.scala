package org.quantlib.indexes

import org.quantlib.time.calendars.BusinessCalendar

/**
  * Created by neo on 02/04/2017.
  */
trait IndexT[I] {
  def name(ofIndex: I): String
  def fixingCalendar[D](ofIndex: I): BusinessCalendar[D]
  def isValidFixingDate[D](ofIndex: I, fixingDate: D): Boolean
  def fixing[D](ofIndex: I, fixingDate: D,forecastTodaysFixing: Boolean): Boolean
}
