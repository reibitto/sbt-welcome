lazy val root = (project in file(".")).
  settings(
    name := "sbt-welcome",
    version := "0.2.1-SNAPSHOT",
    organization := "com.github.reibitto",
    scalaVersion := "2.12.10",
    sbtPlugin := true
  )
