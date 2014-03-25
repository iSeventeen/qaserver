import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "qaserver"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc
    , anorm
    , "mysql" % "mysql-connector-java" % "5.1.21"
    , "postgresql" % "postgresql" % "9.1-901.jdbc4"
//    , "com.github.tototoshi" %% "play-flyway" % "0.2.0"
    , "com.wordnik" % "swagger-play2_2.10" % "1.2.5"
//    , "com.typesafe.slick" %% "slick" % "2.0.1"
//    , "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    playAssetsDirectories <+= baseDirectory / "swagger-ui"
  )

}
