name := """play-scala-fileupload-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += ws
libraryDependencies += guice
libraryDependencies += "com.github.java-json-tools" % "json-schema-validator" % "2.2.8"
val appDependencies = Seq(
  "org.apache.commons" % "commons-io" % "1.3.2",
  javaCore,
  javaJdbc,
)
