val ScalaJsReactVer = "2.1.1"
val circeVersion = "0.14.1"
val Http4sVersion = "0.23.8"

lazy val commonSettings = Seq(
  scalaVersion := "3.3.1",
  scalacOptions ++= Seq()
)

lazy val shared = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "dev.optics" %%% "monocle-core" % "3.2.0",
      "dev.optics" %%% "monocle-macro" % "3.2.0",
      //support for java.time instances in Scala.JS
      "io.github.cquiroz" %%% "scala-java-time" % "2.5.0",
      "co.fs2" %%% "fs2-core" % "3.9.2",
      "co.fs2" %%% "fs2-scodec" % "3.9.2",
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
