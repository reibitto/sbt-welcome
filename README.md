# SBT Welcome

*An SBT plugin for displaying a welcome message and commonly used tasks.*

## What is it?

Upon loading SBT, a welcome message is displayed with common tasks for that project. This is particularly useful
for first-time contributors who are not yet familiar with a project and its build setup. Rather than leave them wondering
"How do I run the benchmarks?", "How do I build the microsite/docs?", etc. you can display these commands on startup.
Here's an example of what it can look like:

![screenshot](assets/screenshot.png?raw=true "SBT Welcome screenshot")

The bullet points are aliases (which are configurable), meaning you can type `d` and it'll run the 4th line item. The
aliases are optional, but can be useful for particularly long commands.

## Installation

Add the following to `project/plugins.sbt`:

```scala
addSbtPlugin("com.github.reibitto" % "sbt-welcome" % "0.1.0")
```

## Configuration

An example configuration:

```scala
import sbtwelcome._

logo :=
  """
    |,---.                           .   ,---.         .
    ||     ,-. ,-,-. ,-,-. ,-. ,-. ,-|   |     ,-. ,-. |- ,-. ,-.
    ||     | | | | | | | | ,-| | | | |   |     |-' | | |  |-' |
    |`---' `-' ' ' ' ' ' ' `-^ ' ' `-'   `---' `-' ' ' `' `-' '  """.stripMargin

usefulTasks := Seq(
  UsefulTask("a", "~compile", "Compile all modules with file-watch enabled"),
  UsefulTask("b", "cli-client/run", "Run Command Center CLI client (interactive mode by default)"),
  UsefulTask("c", "cli-client/assembly", "Create an executable JAR for running command line utility"),
  UsefulTask("d", "cli-client/graalvm-native-image:packageBin", "Create a native executable of the CLI client"),
  UsefulTask("e", "benchmarks/jmh:run", "Run the benchmarks")
)

```

You can also change the default colors like so:

- `logoColor := Console.RED`
- `aliasColor := Color.CYAN`
- `commandColor := Color.YELLOW`
- `descriptionColor := Color.GREEN`
