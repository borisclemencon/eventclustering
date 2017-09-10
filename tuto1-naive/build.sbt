name := "eventclustering"

val versions = Map(
  "spark" -> "2.2.0",
  "aws" -> "1.11.179",
  "scala" -> "2.11.11"
)

version := "0.1"

scalaVersion := versions("scala")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % versions("scala")  % Compile,
  "jline" % "jline" % "2.12.1" % Compile,
  "org.scalatest" %% "scalatest" % "3.0.0"
)

parallelExecution in Test := true