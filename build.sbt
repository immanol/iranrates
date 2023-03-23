enablePlugins(ScalaJSPlugin, NpmPackagePlugin, ScalaJSBundlerPlugin)

name                            := "iran-rates"
version                         := "0.1.0"
scalaVersion                    := "2.13.10"
scalaJSUseMainModuleInitializer := true

npmPackageAuthor       := "Immanol"
npmPackageDescription  := "CLI To Find Iran's Exchange Rates"
npmPackageName         := "iranrates"
npmPackageNpmrcScope   := Some("immanol")
npmPackageBinaryEnable := true
npmPackageDependencies ++= {
  Seq(
    "prompts"            -> "^2.4.2",
    "source-map-support" -> "^0.5.21",
    "puppeteer"          -> "^19.7.3"
  )
}

Compile / npmDependencies ++= Seq(
  "puppeteer" -> "19.7.3",
  "prompts"   -> "2.4.2"
)

Compile / mainClass := Some("immanol.Main")

libraryDependencies ++= Seq(
  "com.monovore"  %%% "decline-effect"      % "2.4.1",
  "org.scala-js"  %%% "scalajs-dom"         % "2.2.0",
  "org.scalameta" %%% "munit"               % "0.7.29" % Test,
  "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7"  % Test
)

testFrameworks += new TestFramework("munit.Framework")
