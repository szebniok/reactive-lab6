enablePlugins(GatlingPlugin)

name := """reactive-lab6"""

version := "1.3"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.26"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-http"   % "10.1.10",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
  "io.gatling" % "gatling-http" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test")

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.1" % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "3.0.1" % "test,it"

