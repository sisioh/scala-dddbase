import sbt._
import Keys._

object DDDBaseBuild extends Build {

   //val specs2Test = "org.specs2" %% "specs2" % "1.5" % "test"
   val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
   val junit = "junit" % "junit" % "4.8.1" % "test"
   val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"

   ///val = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

  // NOTE: New Group ID from Scalaz 6.x and onwards
   val scalaz = "org.scalaz" %% "scalaz-core" % "6.0.4"


   lazy val commonSettings = Defaults.defaultSettings ++ Seq(
      organization := "org.sisioh",
      version := "0.0.1",
      scalaVersion := "2.9.1",
      crossScalaVersions := Seq("2.9.1", "2.10.0"),
      sbtVersion := "0.12.2",
      libraryDependencies ++= Seq(junit,mockito,scalaTest,scalaz),
      scalacOptions ++= Seq("-unchecked", "-deprecation"),
      shellPrompt := { "sbt (%s)> " format projectId(_) },
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publish
      )
  val publish = publishTo <<= (version) { version: String =>
    if (version.trim.endsWith("SNAPSHOT")) {
      Some(Resolver.file("snaphost", new File("./repos/snapshot")))
    }else{
      Some(Resolver.file("release", new File("./repos/release")))
    }
  }

    lazy val root:Project = Project("scala-dddbase",
                            file("."),
                            settings = commonSettings,
          aggregate = Seq(core, spec))

    val core:Project = Project("scala-dddbase-core",
                       file("scala-dddbase-core"),
                       settings = commonSettings)

    val spec:Project = Project("scala-dddbase-spec",
                       file("scala-dddbase-spec"),
                       settings = commonSettings)

  def projectId(state: State) = extracted(state).currentProject.id
  def extracted(state: State) = Project extract state

}
