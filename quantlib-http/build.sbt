name := "quantlib-http"

version := "1.0"

scalaVersion := "2.12.1"

scalacOptions := Seq("-encoding", "utf8")


libraryDependencies ++= akka ++ database ++ quantlibs

lazy val akka = {
  val akkaV       = "2.4.16"
  val akkaHttpV   = "10.0.1"
  val scalaTestV  = "3.0.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

lazy val database = {
  Seq()
}

lazy val quantlibs = {
  val libV = "1.0"
  Seq("org.quantlib" %% "quantlib-temporal" % libV)
}