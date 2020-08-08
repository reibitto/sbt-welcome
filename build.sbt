lazy val root = (project in file(".")).
  settings(
    name := "sbt-welcome",
    version := "0.1.0-SNAPSHOT",
    organization := "com.github.reibitto",
    scalaVersion := "2.12.10",
    sbtPlugin := true
  )
