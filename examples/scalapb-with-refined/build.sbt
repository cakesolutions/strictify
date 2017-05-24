scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "net.cakesolutions" %% "strictify-scalapb" % "0.0.1-SNAPSHOT",
  "net.cakesolutions" %% "strictify-refined" % "0.0.1-SNAPSHOT",
  "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)