import sbt._
import Keys._

object DDDBaseBuild extends Build {
  val specs2 = "org.specs2" %% "specs2" % "1.14" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val junit = "junit" % "junit" % "4.8.1" % "test"
  val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.sisioh",
    version := "0.1.21-SNAPSHOT",
    scalaVersion := "2.10.2",
    libraryDependencies ++= Seq(junit, scalaTest, mockito, scalaTest, specs2),
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),
    shellPrompt := {
      "sbt (%s)> " format projectId(_)
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := {
      _ => false
    },
    publishTo <<= version {
      (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := (
      <url>https://github.com/sisioh/sisioh-dddbase</url>
        <licenses>
          <license>
            <name>Apache License Version 2.0</name>
            <url>http://www.apache.org/licenses/</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:sisioh/sisioh-dddbase.git</url>
          <connection>scm:git:git@github.com:sisioh/sisioh-dddbase.git</connection>
        </scm>
        <developers>
          <developer>
            <id>j5ik2o</id>
            <name>Junichi Kato</name>
            <url>http://j5ik2o.me</url>
          </developer>
        </developers>
      )
  )

  val root = Project(
    id = "scala-dddbase",
    base = file("."),
    settings = commonSettings,
    aggregate = Seq(core, spec, event)
  )

  val core = Project(
    id = "scala-dddbase-core",
    base = file("scala-dddbase-core"),
    settings = commonSettings
  )

  val spec = Project(
    id = "scala-dddbase-spec",
    base = file("scala-dddbase-spec"),
    settings = commonSettings
  ) dependsOn(core)

  val event = Project(
    id = "scala-dddbase-event",
    base = file("scala-dddbase-event"),
    settings = commonSettings
  ) dependsOn(core)

  def projectId(state: State) = extracted(state).currentProject.id

  def extracted(state: State) = Project extract state

//  def publish = publishTo <<= (version) {
//    version: String =>
//      if (version.trim.endsWith("SNAPSHOT")) {
//        Some(Resolver.file("snaphost", new File("./repos/snapshot")))
//      } else {
//        Some(Resolver.file("release", new File("./repos/release")))
//      }
//  }

}
