import sbtprotoc.ProtocPlugin.autoImport.PB


lazy val protocol = project.settings(
  scalaVersion := "2.12.2",
  libraryDependencies ++= Seq(
    "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
  ),
  PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value)
)

lazy val app = project.in(file(".")).dependsOn(protocol).settings(
  scalaVersion := "2.12.2",
  libraryDependencies ++= Seq(
    "net.cakesolutions" %% "strictify-scalapb" % "0.0.1-SNAPSHOT",
    "net.cakesolutions" %% "strictify-refined" % "0.0.1-SNAPSHOT"
  )
)