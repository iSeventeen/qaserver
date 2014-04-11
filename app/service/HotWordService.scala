package service


import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.Play.current
import play.api.db.DB
import models._
import scala.slick.lifted.TableQuery

trait HotWordService {
  
  def findAll(): List[HotWords]
  
  def findById(id: Long): Option[HotWords]

  def save(hotword: HotWords): Int
}

class HotWordServiceImpl extends HotWordService {
  def mgDatabase = Database.forDataSource(DB.getDataSource("default"))
  val hotwords = TableQuery[HotWordsTable]
  
  def findAll(): List[HotWords] = mgDatabase withSession { implicit session =>
    hotwords.list()
  }
  
  def findById(id: Long): Option[HotWords] = mgDatabase withSession { implicit session =>
    hotwords.where(_.id === id).firstOption
  }
  
  def save(hotword: HotWords): Int = mgDatabase withSession { implicit session =>
    hotwords.insert(hotword)
  }
}
