scalaVersion := "2.9.2"

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.1")

resolvers += "less is" at "http://repo.lessis.me"

addSbtPlugin("me.lessis" % "sbt-growl-plugin" % "0.1.3")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")
