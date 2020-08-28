package sbtwelcome

import sbt.Keys._
import sbt.{ Def, _ }

import scala.{ Console => SConsole }

object WelcomePlugin extends AutoPlugin {
  object autoImport {
    val logo             = settingKey[String]("logo")
    val usefulTasks      = settingKey[Seq[UsefulTask]]("usefulTasks")
    val logoColor        = settingKey[String]("logoColor")
    val aliasColor       = settingKey[String]("aliasColor")
    val commandColor     = settingKey[String]("commandColor")
    val descriptionColor = settingKey[String]("descriptionColor")

    val welcome = taskKey[Unit]("print welcome message")
  }

  import autoImport._

  override def trigger = allRequirements

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    logo := """
              |  ______                           _
              | |  ____|                         | |
              | | |__  __  ____ _ _ __ ___  _ __ | | ___
              | |  __| \ \/ / _` | '_ ` _ \| '_ \| |/ _ \
              | | |____ >  < (_| | | | | | | |_) | |  __/
              | |______/_/\_\__,_|_| |_| |_| .__/|_|\___|
              |                            | |
              |                            |_|           """.stripMargin,
    usefulTasks := Nil,
    welcome in LocalRootProject := welcomeTask.value,
    logoColor := SConsole.GREEN,
    aliasColor := SConsole.MAGENTA,
    commandColor := SConsole.CYAN,
    onLoadMessage := {
      // Need to color each line because SBT resets the color for each line when printing `[info]`
      val renderedLogo = logo.value.linesIterator.map { line =>
        s"${logoColor.value}$line"
      }.mkString("\n")

      val renderedCommands = usefulTasks.value.map { u =>
        val bulletPoint =
          if (u.alias.isEmpty)
            s"${aliasColor.value}> ${SConsole.RESET}"
          else
            s"${aliasColor.value}${u.alias}${SConsole.RESET}."

        val description =
          if (u.description.isEmpty)
            ""
          else
            s" - ${u.description}"

        s"$bulletPoint ${commandColor.value}${u.command}${SConsole.RESET}${description}"
      }.mkString("\n")

      s"$renderedLogo${SConsole.RESET}\nUseful sbt tasks:\n$renderedCommands"
    },
    onLoad in GlobalScope += { (initialState: State) =>
      usefulTasks.value.foldLeft(initialState) {
        case (accState, task) =>
          if (task.alias.isEmpty) accState
          else BasicCommands.addAlias(accState, task.alias, task.command)
      }
    },
    onUnload in GlobalScope += { (initialState: State) =>
      usefulTasks.value.foldLeft(initialState) {
        case (accState, task) =>
          if (task.alias.isEmpty) accState
          else BasicCommands.removeAlias(accState, task.alias)
      }
    }
  )

  lazy val welcomeTask =
    Def.task {
      println((onLoadMessage in LocalRootProject).value)
    }

}

final case class UsefulTask(alias: String, command: String, description: String)
