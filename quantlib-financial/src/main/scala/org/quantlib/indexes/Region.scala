package org.quantlib.indexes

sealed abstract class Region(val name: String,val code: String) {

  def ==(other: Region): Boolean = this.name == other.name
  def !=(other: Region): Boolean = this.name != other.name
}

final case class CustomRegion(override val name: String, override val code: String) extends Region(name, code)

case object AustraliaRegion extends Region("Australia", "AU")

case object EURegion extends Region("EU", "EU")

case object FranceRegion extends Region("France", "FR")

case object UKRegion extends Region("UK", "UK")

case object USRegion extends Region("USA", "US")

case object ZARegion extends Region("South Africa", "ZA")

