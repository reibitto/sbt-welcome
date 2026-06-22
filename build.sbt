import sbtwelcome.*

val scala2Version = "2.12.18"
val scala3Version = "3.8.4"

lazy val root = (project in file(".")).settings(
  name := "sbt-welcome",
  scalaVersion := scala3Version,
  crossScalaVersions := Seq(scala2Version, scala3Version),
  sbtPlugin := true,
  libraryDependencies += "org.scalameta" %% "munit" % "1.3.3" % Test,
  pluginCrossBuild / sbtVersion := {
    scalaBinaryVersion.value match {
      case "2.12" => "1.12.12"
      case _      => "2.0.0"
    }
  },
  conflictWarning := {
    scalaBinaryVersion.value match {
      case "3" => ConflictWarning("warn", Level.Warn, failOnConflict = false)
      case _   => conflictWarning.value
    }
  }
)

addCommandAlias("fmt", "all root/scalafmtSbt root/scalafmtAll")
addCommandAlias("fmtCheck", "all root/scalafmtSbtCheck root/scalafmtCheckAll")

LocalRootProject / logo :=
  s"""
     |       ______ _____                   ______
     |__________  /___  /_   ___      _________  /__________________ ________
     |__  ___/_  __ \\  __/   __ | /| / /  _ \\_  /_  ___/  __ \\_  __ `__ \\  _ \\
     |_(__  )_  /_/ / /_     __ |/ |/ //  __/  / / /__ / /_/ /  / / / / /  __/
     |/____/ /_.___/\\__/     ____/|__/ \\___//_/  \\___/ \\____//_/ /_/ /_/\\___/
     |
     |${version.value}
     |
     |${scala.Console.YELLOW}Scala ${scalaVersion.value}${scala.Console.RESET}
     |
     |""".stripMargin

LocalRootProject / usefulTasks := Seq(
  UsefulTask("~compile", "Compile with file-watch enabled"),
  UsefulTask("test", "Run all tests"),
  UsefulTask("fmt", "Run scalafmt on the entire project"),
  UsefulTask("publishLocal", "Publish the sbt plugin locally so that you can consume it from a different project")
)

LocalRootProject / logoColor := scala.Console.MAGENTA

ThisBuild / organization := "com.github.reibitto"
ThisBuild / homepage := Some(url("https://github.com/reibitto/sbt-welcome"))
ThisBuild / licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / developers := List(
  Developer("reibitto", "reibitto", "reibitto@users.noreply.github.com", url("https://reibitto.github.io"))
)
ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}
