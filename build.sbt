import sbtwelcome._

lazy val root = (project in file(".")).settings(
  name         := "sbt-welcome",
  version      := "0.2.2-SNAPSHOT",
  organization := "com.github.reibitto",
  scalaVersion := "2.12.15",
  sbtPlugin    := true
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
     |                              ${version.value}
     |${scala.Console.YELLOW}Scala ${scalaVersion.value}${scala.Console.RESET}
     |
     |""".stripMargin

usefulTasks := Seq(
  UsefulTask("a", "~compile", "Compile with file-watch enabled"),
  UsefulTask("b", "fmt", "Run scalafmt on the entire project"),
  UsefulTask("c", "publishLocal", "Publish the sbt plugin locally so that you can consume it from a different project")
)

logoColor := scala.Console.MAGENTA
