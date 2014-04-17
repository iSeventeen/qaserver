package service

import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._

import play.api.Play.current
import play.api.db.DB

import models._

trait ParentService {

  def all(): List[Parent]

  def findByStudent(cardId: Long): List[Parent]

  def save(parent: Parent): Int
}

class ParentServiceImpl extends ParentService {

  def mgDatabase = Database.forDataSource(DB.getDataSource("default"))

  val parents = TableQuery[ParentTable]

  def all(): List[Parent] = mgDatabase withSession { implicit session =>
    parents.list
  }

  def findByStudent(id: Long): List[Parent] = mgDatabase withSession { implicit session =>
    parents.where(_.student === id).list()
  }

  def save(parent: Parent): Int = mgDatabase withSession { implicit session =>
    parents.insert(parent)
  }
}
