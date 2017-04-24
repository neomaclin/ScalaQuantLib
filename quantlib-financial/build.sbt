name := "quantlib-financial"

version := "1.0"

scalaVersion := "2.11.8"

lazy val catz = Seq("org.typelevel" %% "cats" % "0.9.0")

lazy val scalaTest = Seq("org.scalatest" %% "scalatest" % "3.0.0" % "test")

lazy val localDependencies = Seq("org.quantlib" %% "quantlib-temporal" % "1.0",
  "org.quantlib" %% "quantlib-numeric" % "1.0")


libraryDependencies ++= scalaTest ++ localDependencies ++ catz

