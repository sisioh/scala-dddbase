import com.typesafe.sbt.SbtScalariform

import scalariform.formatter.preferences._

val specs2 = "org.specs2" %% "specs2" % "2.3.12" % "test"

val scalaTest = "org.scalatest" %% "scalatest" % "2.2.0" % "test"

val junit = "junit" % "junit" % "4.8.1" % "test"

val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"

def projectId(state: State) = extracted(state).currentProject.id

def extracted(state: State) = Project extract state

lazy val root = project.in(file("."))
  .settings(name := "scala-dddbase")
  .settings(commonSettings: _*)
  .aggregate(coreJS, coreJVM, forwardingJS, forwardingJVM, memoryJVM, memoryJS, specJVM, specJS)

lazy val scalariformSettings = SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences :=
    ScalariformKeys.preferences.value
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
)

lazy val commonSettings = scalariformSettings ++ Seq(
  sonatypeProfileName := "org.sisioh",
  organization := "org.sisioh",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.5", "2.11.7"),
  scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),
  shellPrompt := {
    "sbt (%s)> " format projectId(_)
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := {
    _ => false
  },
  pomExtra := (
    <url>https://github.com/sisioh/scala-dddbase</url>
      <licenses>
        <license>
          <name>Apache License Version 2.0</name>
          <url>http://www.apache.org/licenses/</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:sisioh/scala-dddbase.git</url>
        <connection>scm:git:git@github.com:sisioh/scala-dddbase.git</connection>
      </scm>
      <developers>
        <developer>
          <id>j5ik2o</id>
          <name>Junichi Kato</name>
          <url>http://j5ik2o.me</url>
        </developer>
      </developers>
    ),
  credentials := {
    val ivyCredentials = (baseDirectory in LocalRootProject).value / ".credentials"
    Credentials(ivyCredentials) :: Nil
  }
)

lazy val jvmCommonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
    mockito,
    specs2,
    scalaTest
  )
)

lazy val jsCommonSettings = Seq(
  scalaJSOutputMode := org.scalajs.core.tools.javascript.OutputMode.ECMAScript6StrongMode
)

lazy val core = crossProject.in(file("scala-dddbase-core")).
  settings(
    name := "scala-dddbase-core"
  )
  .settings(commonSettings: _*)
  .jvmSettings(jvmCommonSettings: _*)
  .jsSettings(jsCommonSettings: _*)

lazy val coreJVM = core.jvm

lazy val coreJS = core.js

lazy val forwarding = crossProject.in(file("scala-dddbase-lifecycle-repositories-forwarding"))
  .settings(
    name := "scala-dddbase-lifecycle-repositories-forwarding"
  )
  .settings(commonSettings: _*)
  .jvmSettings(jvmCommonSettings: _*)
  .jsSettings(jsCommonSettings: _*)
  .dependsOn(core)

lazy val forwardingJVM = forwarding.jvm

lazy val forwardingJS = forwarding.js

lazy val memory = crossProject.in(file("scala-dddbase-lifecycle-repositories-memory"))
  .settings(
    name := "scala-dddbase-lifecycle-repositories-memory"
  )
  .settings(commonSettings: _*)
  .jvmSettings(jvmCommonSettings: _*)
  .jsSettings(jsCommonSettings: _*)
  .dependsOn(core, forwarding)

lazy val memoryJVM = memory.jvm

lazy val memoryJS = memory.js

lazy val spec = crossProject.in(file("scala-dddbase-spec"))
  .settings(
    name := "scala-dddbase-spec"
  )
  .settings(commonSettings: _*)
  .jvmSettings(jvmCommonSettings: _*)
  .jsSettings(jsCommonSettings: _*)
  .dependsOn(core, forwarding)

lazy val specJVM = spec.jvm

lazy val specJS = spec.js
