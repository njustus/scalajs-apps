val ScalaJsReactVer = "2.1.1"
val circeVersion = "0.14.1"
val Http4sVersion = "0.23.8"
val fs2Version = "3.9.2"
val scalaTestVersion = "3.2.17"
val monocleVersion = "3.2.0"

lazy val commonSettings = Seq(
  scalaVersion := "3.4.2",
  scalacOptions ++= Seq(),
  libraryDependencies ++= Seq(
      "org.scalactic" %%% "scalactic" % scalaTestVersion % "test",
      "org.scalatest" %%% "scalatest" % scalaTestVersion % "test",
      "org.scalatestplus" %% "scalacheck-1-17" % (scalaTestVersion+".0") % "test"
  )
)

lazy val shared = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "dev.optics" %%% "monocle-core" % monocleVersion,
      "dev.optics" %%% "monocle-macro" % monocleVersion,
      //support for java.time instances in Scala.JS
      "io.github.cquiroz" %%% "scala-java-time" % "2.5.0",
      "io.github.arainko" %%% "ducktape" % "0.1.11",
      "co.fs2" %%% "fs2-core" % fs2Version,
      "co.fs2" %%% "fs2-scodec" % fs2Version
    )
  )
 .jvmSettings()
 .jsSettings()

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared.js)
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++=Seq(
        //"com.github.japgolly.scalajs-react" %%% "core" % ScalaJsReactVer,
        // Mandatory
        "com.github.japgolly.scalajs-react" %%% "core-bundle-cats_effect"  % ScalaJsReactVer,
        // Optional utils exclusive to scalajs-react
        "com.github.japgolly.scalajs-react" %%% "extra"                    % ScalaJsReactVer,
        // Optional extensions to `core` & `extra` for Monocle
        "com.github.japgolly.scalajs-react" %%% "extra-ext-monocle2"       % ScalaJsReactVer,
        "com.github.japgolly.scalajs-react" %%% "extra-ext-monocle3"       % ScalaJsReactVer,
        "org.scala-js" %%% "scalajs-dom" % "2.1.0",
      )
  )

// lazy val backend = (project in file("backend"))
//   .dependsOn(shared.jvm)
//   .settings(commonSettings)
//   .settings(
//     fork := true,
//     connectInput := true,
//     libraryDependencies ++= Seq(
//       "org.http4s" %% "http4s-ember-server" % Http4sVersion,
//       "org.http4s" %% "http4s-ember-client" % Http4sVersion,
//       "org.http4s" %% "http4s-circe" % Http4sVersion,
//       "org.http4s" %% "http4s-dsl" % Http4sVersion,
//       "ch.qos.logback" % "logback-classic" % "1.2.10",
//       "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
//     )
//   )
