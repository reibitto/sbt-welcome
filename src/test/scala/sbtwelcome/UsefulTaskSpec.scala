package sbtwelcome

import sbtwelcome.WelcomePlugin.Defaults

import scala.collection.compat.immutable.LazyList

class UsefulTaskSpec extends munit.FunSuite {

  test("render tasks (custom alias + empty alias)") {
    val usefulTasks = Seq(
      UsefulTask("task1", "description1").alias("a"),
      UsefulTask("task2", "").noAlias,
      UsefulTask("task3", "description3").noAlias
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = Defaults.autoAliasGen,
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|a. task1 - description1
         |*  task2
         |*  task3 - description3""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  test("render tasks (auto alias)") {
    val usefulTasks = Seq(
      UsefulTask("task1", "description1"),
      UsefulTask("task2", ""),
      UsefulTask("task3", "description3"),
      UsefulTask("task4", ""),
      UsefulTask("task5", "")
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = Defaults.autoAliasGen,
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|a. task1 - description1
         |b. task2
         |c. task3 - description3
         |d. task4
         |e. task5""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  test("render tasks (mixed auto alias)") {
    val usefulTasks = Seq(
      UsefulTask("task1", "description1").noAlias.aliasPrefix("*   "),
      UsefulTask("task2", "").aliasSuffix(" - "),
      UsefulTask("task3", "description3").aliasSuffix(" > "),
      UsefulTask("sub-task3", "").noAlias.aliasPrefix("    - "),
      UsefulTask("task4", "")
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = Defaults.autoAliasGen,
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|*   task1 - description1
         |a - task2
         |b > task3 - description3
         |    - sub-task3
         |c. task4""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  test("render tasks (auto alias should find next available alias if it's already been used)") {
    val usefulTasks = Seq(
      UsefulTask("task1", "description1"),
      UsefulTask("task2", "description2").alias("b"),
      UsefulTask("task3", "description3"),
      UsefulTask("task4", "description4").alias("c"),
      UsefulTask("task5", "description5").alias("a"),
      UsefulTask("task6", "description6")
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = Defaults.autoAliasGen,
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|d. task1 - description1
         |b. task2 - description2
         |e. task3 - description3
         |c. task4 - description4
         |a. task5 - description5
         |f. task6 - description6""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  test("render tasks (custom format)") {
    def customFormatFn: String => String =
      a => if (a.isEmpty) "( ) " else s"($a) "

    val usefulTasks = Seq(
      UsefulTask("task1", "description1").formatAlias(customFormatFn),
      UsefulTask("task2", "description2").formatAlias(customFormatFn),
      UsefulTask("task3", "description3").noAlias.formatAlias(customFormatFn),
      UsefulTask("task4", "description4").alias("Y").formatAlias(customFormatFn),
      UsefulTask("task5", "description5").formatAlias(customFormatFn)
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = Defaults.autoAliasGen,
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|(a) task1 - description1
         |(b) task2 - description2
         |( ) task3 - description3
         |(Y) task4 - description4
         |(c) task5 - description5""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  test("render tasks (default to empty if no aliases left in generator)") {
    val usefulTasks = Seq(
      UsefulTask("task1", "description1"),
      UsefulTask("task2", "description2"),
      UsefulTask("task3", "description3"),
      UsefulTask("task4", "description4"),
      UsefulTask("task5", "description5")
    )

    val actual = WelcomePlugin.renderCommands(
      usefulTasks = usefulTasks,
      autoAliasGen = LazyList("A", "B", "C"),
      aliasColor = "",
      commandColor = "",
      descriptionColor = ""
    )

    val expected =
      """|A. task1 - description1
         |B. task2 - description2
         |C. task3 - description3
         |*. task4 - description4
         |*. task5 - description5""".stripMargin

    assertEquals(stripAnsiCodes(actual), expected)
  }

  def stripAnsiCodes(s: String): String = s.replaceAll("\u001b\\[[;\\d]*m", "")
}
