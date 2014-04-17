package models

import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._

import scala.slick.lifted._

case class Position(
  id: Option[Long],
  name: String)

class PositionTable(tag: Tag) extends Table[Grade](tag, "grade") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)

  def * = (id.?, name) <> (Grade.tupled, Grade.unapply _)
}
