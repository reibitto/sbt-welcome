package sbtwelcome

sealed trait UsefulTaskAlias

object UsefulTaskAlias {
  case object Auto extends UsefulTaskAlias
  case object Empty extends UsefulTaskAlias
  final case class Custom(alias: String) extends UsefulTaskAlias
}
