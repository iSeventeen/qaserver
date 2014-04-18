package models

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._
import scala.slick.lifted.Tag
import org.joda.time.DateTime

import org.joda.time.DateTime

case class Staff(
  id: Option[Long],
  cardId: String,
  name: String,
  grade: Int,
  position: Int,
  phone: String,
  updatedAt: DateTime)

class StaffTable(tag: Tag) extends Table[Staff](tag, "staff") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cardId = column[String]("cardId", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def grade = column[Int]("grade")
  def position = column[Int]("position")
  def phone = column[String]("phone")
  def updatedAt = column[DateTime]("update_at")

  def * = (id.?, cardId, name, grade, position, phone, updatedAt) <> (Staff.tupled, Staff.unapply _)
}
