package models

import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._

import scala.slick.lifted._

case class JobTitle(
  id: Option[Long],
  name: String)

class JobTitleTable(tag: Tag) extends Table[JobTitle](tag, "job_title") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)

  def * = (id.?, name) <> (JobTitle.tupled, JobTitle.unapply _)
}
