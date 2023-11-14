package sbtwelcome

import scala.Console as SConsole

final case class UsefulTask(
    command: String,
    description: String,
    alias: UsefulTaskAlias,
    renderAliasFn: String => String
) {

  def autoAlias: UsefulTask = copy(alias = UsefulTaskAlias.Auto)

  def noAlias: UsefulTask =
    copy(alias = UsefulTaskAlias.Empty, renderAliasFn = _ => s"*  ")

  def alias(alias: String): UsefulTask =
    copy(alias = UsefulTaskAlias.Custom(alias))

  def aliasPrefix(prefix: String): UsefulTask =
    copy(renderAliasFn = a => s"$prefix$a")

  def aliasSuffix(suffix: String): UsefulTask =
    copy(renderAliasFn = a => s"$a$suffix")

  def formatAlias(renderFn: String => String): UsefulTask =
    copy(renderAliasFn = renderFn)
}

object UsefulTask {

  def apply(command: String, description: String): UsefulTask =
    new UsefulTask(command, description, UsefulTaskAlias.Auto, a => s"$a${SConsole.RESET}. ")
}
