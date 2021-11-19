val scala3Version = "3.1.0"

lazy val alga = project
  .in(file("."))
  .settings(
    name := "alga",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"
  )
