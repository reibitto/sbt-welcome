import sbtwelcome._

inThisBuild(
  List(
    organization := "com.github.reibitto",
    homepage := Some(url("https://github.com/reibitto/sbt-welcome")),
    licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer("reibitto", "reibitto", "reibitto@users.noreply.github.com", url("https://reibitto.github.io"))
    )
  )
)

lazy val root = (project in file(".")).settings(
  name := "sbt-welcome",
  organization := "com.github.reibitto",
  scalaVersion := "2.12.18",
  sbtPlugin := true,
  libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
)

addCommandAlias("fmt", "all root/scalafmtSbt root/scalafmtAll")
addCommandAlias("fmtCheck", "all root/scalafmtSbtCheck root/scalafmtCheckAll")

logo :=
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

usefulTasks := Seq(
  UsefulTask("~compile", "Compile with file-watch enabled"),
  UsefulTask("test", "Run all tests"),
  UsefulTask("fmt", "Run scalafmt on the entire project"),
  UsefulTask("publishLocal", "Publish the sbt plugin locally so that you can consume it from a different project")
)

logoColor := scala.Console.MAGENTA

ThisBuild / organization := "com.github.reibitto"
