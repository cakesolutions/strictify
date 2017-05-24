lazy val `strictify-core` = project
  .settings(
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.2"
    )
  )

lazy val `strictify-refined` = project
  .dependsOn(`strictify-core` % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined" % "0.8.1"
    )
  )


lazy val `strictify-scalapb` = project
  .dependsOn(`strictify-core` % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.0-pre1"
    )
  )