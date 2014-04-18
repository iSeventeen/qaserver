package models

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._
import scala.slick.lifted.Tag
import org.joda.time.DateTime

import org.joda.time.DateTime

case class Record(
  id: Option[Long],
  cardId: String,
  video: String,
  inOut: Int,
  createdAt: DateTime)

class RecordTable(tag: Tag) extends Table[Record](tag, "record") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cardId = column[String]("cardId", O.NotNull)
  def video = column[String]("video", O.NotNull)
  def inOut = column[Int]("in_out")
  def createdAt = column[DateTime]("created_at")

  def * = (id.?, cardId, video, inOut, createdAt) <> (Record.tupled, Record.unapply _)
}
