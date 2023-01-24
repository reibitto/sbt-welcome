package sbtwelcome

import sbt.Keys._
import sbt.{ Def, _ }

import scala.{ Console => SConsole }
import sbtwelcome.UsefulTaskAlias.Auto
import sbtwelcome.UsefulTaskAlias.Custom
import sbtwelcome.UsefulTaskAlias.Empty

object WelcomePlugin extends AutoPlugin {
  object autoImport {
    val logo              = settingKey[String]("logo")
    val usefulTasks       = settingKey[Seq[UsefulTask]]("usefulTasks")
    val logoColor         = settingKey[String]("logoColor")
    val aliasColor        = settingKey[String]("aliasColor")
    val commandColor      = settingKey[String]("commandColor")
    val descriptionColor  = settingKey[String]("descriptionColor")
    val welcomeEnabled    = settingKey[Boolean]("welcomeEnabled")
    val autoAliasForIndex = settingKey[Int => Option[String]]("autoAliasForIndex")

    val welcome = taskKey[Unit]("print welcome message")
  }

  object Defaults {
    val logoColor: String                        = SConsole.GREEN
    val aliasColor: String                       = SConsole.MAGENTA
    val commandColor: String                     = SConsole.CYAN
    val descriptionColor: String                 = ""
    val welcomeEnabled: Boolean                  = true
    val autoAliasForIndex: Int => Option[String] = { (i: Int) =>
      if (i >= 0 && i < 26) Some(('a' + i).toChar.toString) else None
    }
  }

  import autoImport._

  override def trigger = allRequirements

  def renderLogo(logo: String, logoColor: String): String =
    // Need to color each line because SBT resets the color for each line when printing `[info]`
    logo.linesIterator.map { line =>
      s"$logoColor$line"
    }.mkString("\n")

  def renderCommands(
    usefulTasks: Seq[UsefulTask],
    autoAliasForIndex: Int => Option[String],
    aliasColor: String,
    commandColor: String,
    descriptionColor: String
  ) = {
    var context = AutoAliasContext(0, autoAliasForIndex)

    usefulTasks.map { u =>
      val alias = u.alias match {
        case UsefulTaskAlias.Auto =>
          val alias = context.currentAutoAlias.getOrElse("*") // TODO: Consider making this customizable
          context = context.incrementAutoAlias
          alias

        case UsefulTaskAlias.Empty => ""

        case UsefulTaskAlias.Custom(alias) => alias
      }

      val aliasSection = s"$aliasColor${u.renderAliasFn(alias)}${SConsole.RESET}"

      val description =
        if (u.description.isEmpty)
          ""
        else
          s" - $descriptionColor${u.description}${SConsole.RESET}"

      s"$aliasSection$commandColor${u.command}${SConsole.RESET}$description"
    }.mkString("\n")
  }

  def renderWelcomeMessage(
    usefulTasks: Seq[UsefulTask],
    autoAliasForIndex: Int => Option[String],
    logo: String,
    logoColor: String,
    aliasColor: String,
    commandColor: String,
    descriptionColor: String
  ): String = {
    val renderedLogo     = renderLogo(logo, logoColor)
    val renderedCommands = renderCommands(usefulTasks, autoAliasForIndex, aliasColor, commandColor, descriptionColor)

    val renderedUsefulTasks =
      if (usefulTasks.isEmpty) ""
      else s"Useful sbt tasks:\n$renderedCommands"

    s"$renderedLogo${SConsole.RESET}\n$renderedUsefulTasks"
  }

  private def buildSbtState(
    initialState: State,
    usefulTasks: Seq[UsefulTask],
    autoAliasForIndex: Int => Option[String]
  ) = {
    var context = AutoAliasContext(0, autoAliasForIndex)

    usefulTasks.foldLeft(initialState) { case (accState, task) =>
      val state = task.alias match {
        case Custom(alias) => BasicCommands.addAlias(accState, alias, task.command)
        case Empty => accState
        case Auto =>
          context.currentAutoAlias match {
            case Some(alias) => BasicCommands.addAlias(accState, alias, task.command)
            case None        => accState
          }
      }

      context = context.incrementAutoAlias

      state
    }
  }

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    logo                       := """
              |  ______                           _
              | |  ____|                         | |
              | | |__  __  ____ _ _ __ ___  _ __ | | ___
              | |  __| \ \/ / _` | '_ ` _ \| '_ \| |/ _ \
              | | |____ >  < (_| | | | | | | |_) | |  __/
              | |______/_/\_\__,_|_| |_| |_| .__/|_|\___|
              |                            | |
              |                            |_|
              |
              |Change the `logo` sbt key to insert your own logo (or set it to empty if you prefer to not show it)
              |
              |See the README at https://github.com/reibitto/sbt-welcome for more details.""".stripMargin,
    usefulTasks                := Nil,
    LocalRootProject / welcome := welcomeTask.value,
    logoColor                  := Defaults.logoColor,
    aliasColor                 := Defaults.aliasColor,
    commandColor               := Defaults.commandColor,
    descriptionColor           := Defaults.descriptionColor,
    welcomeEnabled             := Defaults.welcomeEnabled,
    autoAliasForIndex          := Defaults.autoAliasForIndex,
    onLoadMessage              := {
      if (welcomeEnabled.value)
        renderWelcomeMessage(
          usefulTasks.value,
          autoAliasForIndex.value,
          logo.value,
          logoColor.value,
          aliasColor.value,
          commandColor.value,
          descriptionColor.value
        )
      else ""
    },
    onLoad in GlobalScope += { (initialState: State) =>
      buildSbtState(initialState, usefulTasks.value, autoAliasForIndex.value)
    },
    onUnload in GlobalScope += { (initialState: State) =>
      buildSbtState(initialState, usefulTasks.value, autoAliasForIndex.value)
    }
  )

  lazy val welcomeTask =
    Def.task {
      println((LocalRootProject / onLoadMessage).value)
    }

}
