package sbtwelcome

import scala.collection.compat.immutable.LazyList

final case class AutoAliasContext(
    currentAutoAlias: Option[String],
    autoAliasGen: LazyList[String],
    customAliases: Set[String]
) {

  def findNextAutoAlias: AutoAliasContext = {
    // Don't auto generate an alias which has been used by the user with a custom alias.
    val filteredAliases = autoAliasGen.dropWhile { s =>
      customAliases.contains(s)
    }

    copy(
      currentAutoAlias = filteredAliases.headOption,
      autoAliasGen = if (filteredAliases.isEmpty) LazyList.empty else filteredAliases.tail
    )
  }

}

object AutoAliasContext {

  def fromTasks(usefulTasks: Seq[UsefulTask], autoAliasGen: LazyList[String]): AutoAliasContext = {
    val customAliases = usefulTasks.flatMap { task =>
      task.alias match {
        case UsefulTaskAlias.Custom(alias)                => Some(alias)
        case UsefulTaskAlias.Empty | UsefulTaskAlias.Auto => None
      }
    }.toSet

    AutoAliasContext(None, autoAliasGen, customAliases)
  }
}
