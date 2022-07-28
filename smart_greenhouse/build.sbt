ThisBuild / resolvers += Resolver.jcenterRepo

val junitJupiterVersion = "5.7.1"
val junitPlatformVersion = "1.8.2"

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "3.1.2",
    name := "smart_greenhouse",
    libraryDependencies ++= Seq(
      "org.junit.jupiter" % "junit-jupiter-api" % junitJupiterVersion % Test, // aggregator of junit-jupiter-api and junit-jupiter-engine (runtime)
      "org.junit.jupiter" % "junit-jupiter-engine" % junitJupiterVersion % Test, // for org.junit.platform
      "org.junit.vintage" % "junit-vintage-engine" % junitJupiterVersion % Test,
      "org.junit.platform" % "junit-platform-launcher" % junitPlatformVersion % Test,
      "org.junit.platform" % "junit-platform-engine" % junitPlatformVersion % Test,
      "org.junit.platform" % "junit-platform-suite-api" % junitPlatformVersion % Test,
      "org.junit.platform" % "junit-platform-commons" % junitPlatformVersion % Test,
      "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
      "org.scalatest" %% "scalatest" % "3.2.11" % Test,
      "org.scalatestplus" %% "selenium-4-1" % "3.2.11.0" % Test,
      "io.cucumber" %% "cucumber-scala" % "8.2.6" % Test,
      "org.scalatestplus" %% "scalacheck-1-15" % "3.2.10.0" % Test,
      "org.scalatestplus" %% "mockito-3-12" % "3.2.10.0" % Test,
      "com.tngtech.archunit" % "archunit" % "0.18.0" % Test,
      "org.slf4j" % "slf4j-log4j12" % "1.7.26" % Test,
      "com.lihaoyi" %% "requests" % "0.6.9",
      "org.json4s" %% "json4s-jackson" % "4.0.3",
      "org.scalafx" %% "scalafx" % "16.0.0-R24",
      "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1",
      "it.unibo.alice.tuprolog" % "2p-ui" % "4.1.1",
      "org.controlsfx" % "controlsfx" % "11.1.1",
      "org.testfx" % "testfx-core" % "4.0.16-alpha" % Test,
      "org.testfx" % "testfx-junit5" % "4.0.16-alpha" % Test
    ) ++ Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName),
    crossPaths := false, // https://github.com/sbt/junit-interface/issues/35
    Test / parallelExecution := false
  )

// Cucumber configuration
// Run by:  sbt> cucumber
enablePlugins(CucumberPlugin)
CucumberPlugin.glues := List(
  "testLecture/code/e4bdd/steps"
) //testlecture isn't correct for greenhouse project fullpath is test/scala/testLecture/code/e4bdd/steps

wartremoverWarnings ++= Warts.all
/*
lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .settings(
    libraryDependencies ++= Seq(
      "io.monix" %%% "monix" % "3.4.0",
      "dev.optics" %%% "monocle-core" % "3.1.0",
      "dev.optics" %%% "monocle-macro" % "3.1.0"
    )
  )
lazy val swing = project.dependsOn(core.jvm)
lazy val js = project
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core.js)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.2.0"
  )*/
