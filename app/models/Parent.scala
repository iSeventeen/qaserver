package models

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._
import scala.slick.lifted.Tag
import org.joda.time.DateTime

import play.api.db._
import play.api.mvc._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Parent(
  id: Option[Long],
  cardId: String,
  name: String,
  role: String,
  avatar: Option[String],
  phone: String,
  createdAt: Option[DateTime],
  updatedAt: Option[DateTime],
  student: Long) {

}

class ParentTable(tag: Tag) extends Table[Parent](tag, "parent") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cardId = column[String]("card_id", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def role = column[String]("role", O.NotNull)
  def avatar = column[String]("avatar")
  def phone = column[String]("phone")
  def createdAt = column[DateTime]("created_at")
  def updatedAt = column[DateTime]("updated_at")
  def student = column[Long]("student", O.NotNull)

  def * = (id.?, cardId, name, role, avatar.?, phone, createdAt.?, updatedAt.?, student) <> (Parent.tupled, Parent.unapply _)
}
