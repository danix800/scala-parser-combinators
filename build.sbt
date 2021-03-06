scalaVersion in ThisBuild := crossScalaVersions.value.head

crossScalaVersions in ThisBuild := {
  val javaVersion = System.getProperty("java.version")
  val isJDK6Or7 =
    javaVersion.startsWith("1.6.") || javaVersion.startsWith("1.7.")
  if (isJDK6Or7)
    Seq("2.11.8")
  else
    Seq("2.11.8", "2.12.0-RC1")
}

lazy val `scala-parser-combinators` = crossProject.in(file(".")).
  settings(scalaModuleSettings: _*).
  settings(
    name := "scala-parser-combinators-root"
  ).
  jvmSettings(
    // Mima uses the name of the jvm project in the artifactId
    // when resolving previous versions (so no "-jvm" project)
    name := "scala-parser-combinators"
  ).
  jsSettings(
    name := "scala-parser-combinators-js"
  ).
  settings(
    moduleName         := "scala-parser-combinators",
    version            := "1.0.5-SNAPSHOT"
  ).
  jvmSettings(
    // important!! must come here (why?)
    scalaModuleOsgiSettings: _*
  ).
  jvmSettings(
    OsgiKeys.exportPackage := Seq(s"scala.util.parsing.*;version=${version.value}"),

    // needed to fix classloader issues (see scala-xml#20)
    fork in Test := true
  ).
  jsSettings(
    // Scala.js cannot run forked tests
    fork in Test := false
  ).
  jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin)).
  jvmSettings(
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  ).
  jvmSettings(
    mimaPreviousVersion := Some("1.0.4")
  )

lazy val `scala-parser-combinatorsJVM` = `scala-parser-combinators`.jvm
lazy val `scala-parser-combinatorsJS` = `scala-parser-combinators`.js
