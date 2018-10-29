ensimeScalaVersion in ThisBuild := "2.12.1"

val commonSettings = Seq(
  scalaVersion := "2.12.1",
  name := "fpinscala",
  version := "1.0",
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val root = (project in file("."))
  .aggregate(exercises, answers)
  .settings(commonSettings)
  .settings(
    name := "fpinscala"
  )

lazy val exercises = (project in file("exercises"))
  .settings(commonSettings)
  .settings(
    name := "exercises"
  )

lazy val answers = (project in file("answers"))
  .settings(commonSettings)
  .settings(
    name := "answers"
  )

