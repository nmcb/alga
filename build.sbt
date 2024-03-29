lazy val alga = project
  .in(file("."))
  .settings(
    name         := "alga",
    version      := "0.1.0",
    scalaVersion := "3.3.3",

    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.17.0" % "test"
    )
  )
