ivyLoggingLevel := UpdateLogging.Quiet
scalacOptions in Compile ++= Seq("-feature", "-deprecation")

// Cake Standards
addSbtPlugin("net.cakesolutions" % "sbt-cake" % "1.0.2")
