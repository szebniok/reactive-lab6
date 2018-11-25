enablePlugins(GatlingPlugin)

name := """reactive-lab6"""

version := "1.1"

scalaVersion := "2.12.4"

version := "1.2"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-remote" % "2.5.18",
  "com.typesafe.akka" %% "akka-http"   % "10.1.5",
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
  "io.gatling" % "gatling-http" % "3.0.1",
  "org.scalatest" % "scalatest_2.12" % "3.0.0" % "test")

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.1" % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "3.0.1" % "test,it"

