package global

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.googlecode.flyway.core.Flyway
import controllers.Application
import net.codingwell.scalaguice.ScalaModule
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.Play
import play.api.Play.current
import play.api.db.DB
import service.ParentService
import service.ParentServiceImpl
import service.StudentService
import service.StudentServiceImpl
import service.HotWordService
import service.HotWordServiceImpl

object Global extends GlobalSettings {

  class MainModule extends AbstractModule with ScalaModule {
    def configure() {
      bind[StudentService].to[StudentServiceImpl]
      bind[ParentService].to[ParentServiceImpl]
      bind[HotWordService].to[HotWordServiceImpl]

      bind[controllers.Application.type].toInstance(controllers.Application)
    }
  }

  lazy val injector = Guice.createInjector(new MainModule)

  override def onStart(app: Application) {
    Logger.info("Application onStart startig")

    injector

    // run database migrations
    runDatabaseMigrations("default")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...");
  }

  def runDatabaseMigrations(dbName: String) = {
    Logger.info("Database migrations starting")
    val migrationFilesLocation = s"db/migration/${dbName}"
    Play.current.resource(migrationFilesLocation) match {
      case Some(r) => {
        Logger.info(s"Directory for migration files found: ${migrationFilesLocation}")

        val flyway = new Flyway
        flyway.setDataSource(DB.getDataSource(dbName))
        flyway.setLocations(migrationFilesLocation)
        flyway.setInitOnMigrate(true)
        flyway.migrate()
      }
      case None => {
        Logger.error(s"Directory for migration files not found: ${migrationFilesLocation}")
      }
    }
    Logger.info("Database migrations finished")
  }

}
