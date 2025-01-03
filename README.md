# SBT Welcome

![Scala CI](https://github.com/reibitto/sbt-welcome/actions/workflows/scala.yml/badge.svg)

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
addSbtPlugin("com.github.reibitto" % "sbt-welcome" % "0.5.0")
```

## Commands

You can type `welcome` to re-print the welcome message (rather than reloading the project).

## Configuration

An example configuration:

```scala
import sbtwelcome._

logo :=
  s"""
     |       ______ _____                   ______
     |__________  /___  /_   ___      _________  /__________________ ________
     |__  ___/_  __ \\  __/   __ | /| / /  _ \\_  /_  ___/  __ \\_  __ `__ \\  _ \\
     |_(__  )_  /_/ / /_     __ |/ |/ //  __/  / / /__ / /_/ /  / / / / /  __/
     |/____/ /_.___/\\__/     ____/|__/ \\___//_/  \\___/ \\____//_/ /_/ /_/\\___/
     |
     |${version.value}
     |
     |${scala.Console.YELLOW}Scala ${scalaVersion.value}${scala.Console.RESET}
     |
     |""".stripMargin

usefulTasks := Seq(
  UsefulTask("~compile", "Compile with file-watch enabled"),
  UsefulTask("fmt", "Run scalafmt on the entire project"),
  UsefulTask("publishLocal", "Publish the sbt plugin locally so that you can consume it from a different project"),
  UsefulTask("cli-client/graalvm-native-image:packageBin", "Create a native executable of the CLI client"),
  UsefulTask("benchmarks/jmh:run", "Run the benchmarks")
)

logoColor := scala.Console.MAGENTA
```

By default, each task gets auto-assigned an alias (`a` through `z`). This can be customized (details below).

You can embed any other information in the logo, such as the project version with normal Scala string interpolation like:

```scala
logo := s"Some Logo ${version.value}\nScala ${scalaVersion.value}"
```

You can also change the default colors like so:

- `logoColor := scala.Console.RED`
- `aliasColor := scala.Console.CYAN`
- `commandColor := scala.Console.YELLOW`
- `descriptionColor := scala.Console.GREEN`

### Defining a custom alias

```scala
UsefulTask("all root/scalafmtSbt root/scalafmtAll", "formats all Scala files in project").alias("fmt")
```

### Disabling the welcome message

In CI you may want to suppress the welcome message you prefer slightly less noise. To do so, use the `welcomeEnabled`
sbt setting like so:

```scala
welcomeEnabled := false
```

You'll probably want to use an environment variable to set this dynamically so that CI and local development have
different values.

### Further customizing `UsefulTask`

#### Declaring a `UsefulTask` without an alias:

```scala
UsefulTask("task1", "description1").noAlias
```

#### Rendering the alias differently

You can define your own prefix or suffix with `aliasPrefix` and `aliasSuffix`. Or if you want full customization,
there's also `formatAlias` which accepts an arbitrary function.

```scala
UsefulTask("task1", "description1").noAlias.aliasPrefix("* ")

UsefulTask("task2", "description2").noAlias.aliasSuffix(" > ")

def customFormatFn: String => String =
  a => if (a.isEmpty) "( ) " else s"($a) "
  
UsefulTask("task3", "description3").formatAlias(customFormatFn),
```

#### Customize the auto-alias generator

If you want something other than the default auto-aliases (`a`, `b`, `c`, etc.), you can use customize it like so:

```scala
autoAliasGen := LazyList.from(1).map(n => s"t$n")
```

This would generate: `t1`, `t2`, `t3`, etc.

### Logo

If you want to generate a fancy logo for the `logo` field, you can use one of the following tools:

- [embroidery](https://github.com/wi101/embroidery)
- [TAAG](http://patorjk.com/software/taag)
