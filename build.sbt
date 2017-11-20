name := """reactive-lab6"""

version := "1.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-remote" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test")

