package sbtwelcome

final case class AutoAliasContext(taskIndex: Int, autoAliasForIndex: Int => Option[String]) {
  def currentAutoAlias: Option[String] = autoAliasForIndex(taskIndex)

  def incrementAutoAlias: AutoAliasContext = copy(taskIndex = taskIndex + 1)
}
