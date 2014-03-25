package global

import play.api._
import com.googlecode.flyway.core.Flyway
import play.api.db.DB
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application onStart startig")

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
