import scala.util.Properties

import sbt._
import sbt.Keys._

object ProjectPlugin extends AutoPlugin {
  override def requires = net.cakesolutions.CakePlatformPlugin
  override def trigger  = allRequirements

  val autoImport = ProjectPluginKeys
  import autoImport._

  // NOTE: everything in here is applied once, to the entire build
  override val buildSettings = Seq(
    organization := "net.cakesolutions",
    scalaVersion := "2.12.2",
    maxErrors := 1,
    fork := true,
    cancelable := true,
    sourcesInBase := false,
    javaOptions += s"-Dcake.sbt.root=${(baseDirectory in ThisBuild).value.getCanonicalFile}",
    // WORKAROUND https://github.com/dwijnand/sbt-dynver/issues/23
    version := {
      val v = version.value
      if (!v.contains("+")) v
      else v + "-SNAPSHOT"
    },
    concurrentRestrictions := {
      val limited = Properties.envOrElse("SBT_TASK_LIMIT", "4").toInt
      Seq(Tags.limitAll(limited))
    }
  )

  // NOTE: everything in here is applied to every project (a better `commonSettings`)
  override val projectSettings = Seq(
    scalacOptions in (Compile, console) := Seq()
  )

}

object ProjectPluginKeys {
  // NOTE: anything in here is automatically visible in build.sbt
}
