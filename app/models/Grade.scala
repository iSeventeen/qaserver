package models

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._

import scala.slick.lifted._

case class Grade(
  id: Option[Long],
  name: String)

class GradeTable(tag: Tag) extends Table[Grade](tag, "grade") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)

  def * = (id.?, name) <> (Grade.tupled, Grade.unapply _)
}
