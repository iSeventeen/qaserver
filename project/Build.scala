import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "qaserver"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.21",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.googlecode.flyway" % "flyway-core" % "2.3.1",
    "com.typesafe.slick" %% "slick" % "2.0.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.github.nscala-time" %% "nscala-time" % "0.8.0",
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.5",
    "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1",
    "com.google.inject" % "guice" % "3.0",
    "net.codingwell" % "scala-guice_2.10" % "3.0.2",
    "com.wordnik" % "swagger-play2_2.10" % "1.2.5"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    playAssetsDirectories <+= baseDirectory / "swagger-ui"
  )

}
