import sbt.Keys.libraryDependencies
ThisBuild / resolvers += Resolver.jcenterRepo

val junitJupiterVersion = "5.7.1"
val junitPlatformVersion = "1.8.2"

lazy val osNames = Seq("linux", "mac", "win")

assembly / mainClass := Some("it.unibo.pps.smartgh.Main")

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "3.1.2",
    name := "smart_greenhouse",
    assembly / assemblyJarName := "smartgh.jar",
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
      "org.scalatestplus" %% "scalacheck-1-15" % "3.2.10.0" % Test,
      "org.scalatestplus" %% "mockito-3-12" % "3.2.10.0" % Test,
      "com.lihaoyi" %% "requests" % "0.6.9",
      "org.json4s" %% "json4s-jackson" % "4.0.3",
      "org.scalafx" %% "scalafx" % "16.0.0-R24",
      "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1",
      "it.unibo.alice.tuprolog" % "2p-ui" % "4.1.1",
      "org.controlsfx" % "controlsfx" % "11.1.1",
      "org.testfx" % "testfx-core" % "4.0.16-alpha" % Test,
      "org.testfx" % "testfx-junit5" % "4.0.16-alpha" % Test,
      "org.testfx" % "openjfx-monocle" % "jdk-12.0.1+2" % Test,
      "io.monix" %% "monix" % "3.4.0",
      "com.github.nscala-time" %% "nscala-time" % "2.30.0",
      "org.scalactic" %% "scalactic" % "3.2.13"
    ) ++ osNames.flatMap(os =>
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier os) ),
    crossPaths := false, // https://github.com/sbt/junit-interface/issues/35
    Test / parallelExecution := false
  )

wartremoverWarnings ++= Warts.allBut(
  Wart.Any,
  Wart.Var,
  Wart.AsInstanceOf,
  Wart.Null,
  Wart.ThreadSleep,
  Wart.Nothing,
  Wart.Throw,
  Wart.ToString,
  Wart.DefaultArguments,
  Wart.AutoUnboxing,
  Wart.IsInstanceOf
)
