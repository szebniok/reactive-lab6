enablePlugins(GatlingPlugin)

name := """reactive-lab6"""

version := "1.3"

scalaVersion := "2.12.10" //gatling sbt plugin is not adjusted to scala 2.13.x

val akkaVersion = "2.6.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"           % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"           % akkaVersion,
  "com.typesafe.akka" %% "akka-http"            % "10.2.1",
  "io.netty"           % "netty"                % "3.10.6.Final",
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster"         % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit"         % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.1",
  "io.gatling"         % "gatling-http"         % "3.4.1"     % "test, it",
  "org.scalatest"     %% "scalatest"            % "3.2.3"     % "test"
)

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.4.1" % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "3.4.1" % "test,it"
