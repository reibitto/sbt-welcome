package sbtwelcome

sealed trait UsefulTaskAlias {
  def sbtAlias(context: AutoAliasContext): Option[String]
}

object UsefulTaskAlias {
  case object Auto extends UsefulTaskAlias {
    def sbtAlias(context: AutoAliasContext): Option[String] =
      context.autoAliasForIndex(context.taskIndex)
  }

  case object Empty extends UsefulTaskAlias {
    def sbtAlias(context: AutoAliasContext): Option[String] = None
  }

  final case class Custom(alias: String) extends UsefulTaskAlias {
    def sbtAlias(context: AutoAliasContext): Option[String] = None
  }
}
