package global

import play.api._
import play.api.db.DB
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application onStart startig")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...");
  }

}
