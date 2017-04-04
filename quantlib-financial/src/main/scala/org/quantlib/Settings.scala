package org.quantlib

import java.time.LocalDate

// global repository for run-time library settings
// initial value should either sit as config file or from DB

object Settings {
  implicit val default = DefaultSettings()
  final case class DefaultSettings(evaluationDate: LocalDate = LocalDate.now,
                                   includeReferenceDateEvents: Boolean = false,
                                   includeTodaysCashFlows: Option[Boolean] = None,
                                   enforcesTodaysHistoricFixings: Boolean = false
                                  ) extends Settings
}

trait Settings {

  def evaluationDate: LocalDate
  // always use the current date as the default evaluation date
  def includeReferenceDateEvents: Boolean

  def includeTodaysCashFlows: Option[Boolean]

  def enforcesTodaysHistoricFixings: Boolean
}


final case class SystemSettings(evaluationDate: LocalDate  = LocalDate.now,
                                 // always use the current date as the default evaluation date
                                 includeReferenceDateEvents: Boolean,
                                 includeTodaysCashFlows: Option[Boolean],
                                 enforcesTodaysHistoricFixings: Boolean
                                ) extends Settings

