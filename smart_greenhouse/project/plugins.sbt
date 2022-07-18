addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.4.0")
// addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3") // does not work for Scala 3
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.3.1")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.10.0")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.0.5")
addDependencyTreePlugin

resolvers += Resolver.jcenterRepo
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.9.0")
