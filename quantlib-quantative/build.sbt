name := "quantlib-quantative"

version := "1.0"

scalaVersion := "2.11.8"

lazy val nd4s = {
  val nd4jVersion = "0.7.2"
  Seq("org.nd4j" % "nd4j-native-platform" % nd4jVersion,
    "org.nd4j" %% "nd4s" % nd4jVersion)
}

lazy val scalaTest = Seq("org.scalatest" %% "scalatest" % "3.0.1" % "test")

libraryDependencies ++= scalaTest ++  nd4s