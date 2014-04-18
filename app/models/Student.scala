package models

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._
import scala.slick.lifted.Tag
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import org.joda.time.DateTime
import play.api.Logger

case class Student(
  id: Option[Long] = None,
  cardId: Option[String] = None,
  name: String,
  gender: Int,
  grade: Int,
  address: String,
  avatar: Option[String],
  note: Option[String],
  cratedAt: Option[DateTime],
  updatedAt: Option[DateTime]) {

  var parents: List[Parent] = Nil
}

class StudentTable(tag: Tag) extends Table[Student](tag, "student") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cardId = column[String]("card_id", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def gender = column[Int]("gender")
  def grade = column[Int]("grade")
  def address = column[String]("address")
  def avatar = column[String]("avatar")
  def note = column[String]("note")
  def createdAt = column[DateTime]("created_at")
  def updatedAt = column[DateTime]("updated_at")

  def * = (id.?, cardId.?, name, gender, grade, address, avatar.?, note.?, createdAt.?, updatedAt.?) <> (Student.tupled, Student.unapply _)
}
