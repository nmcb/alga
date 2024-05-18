lazy val alga = project
  .in(file("."))
  .settings(
    name         := "alga",
    version      := "0.1.0",
    scalaVersion := "3.4.2",

    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.18.0" % "test"
    )
  )
