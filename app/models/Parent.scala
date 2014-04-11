package models

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.Tag
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._

import play.api.db._
import play.api.mvc._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Parent(
  id: Option[Long],
  name: String,
  role: String,
  avatar: Option[String],
  student: String) {

}

class ParentTable(tag: Tag) extends Table[Parent](tag, "parent") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def role = column[String]("role", O.NotNull)
  def avatar = column[String]("avatar")
  def student = column[String]("student", O.NotNull)

  def * = (id.?, name, role, avatar.?, student) <> (Parent.tupled, Parent.unapply _)
}

case class ParentObj(
  id: Pk[Long],
  name: String,
  role: String,
  avatar: Option[String],
  student: String) {

}

object ParentObj {

  val TABLE_NAME = "parent"

  val sample = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[String]("role") ~
      get[Option[String]]("avatar") ~
      get[String]("student") map {
        case id ~ name ~ role ~ avatar ~ student => ParentObj(id, name, role, avatar, student)
      }
  }

  def findAll(): List[ParentObj] = DB.withConnection { implicit connection =>

    SQL("select * from parent") as (ParentObj.sample *)

  }

  def findByStudent(cardId: String): List[ParentObj] = DB.withConnection { implicit connection =>

    SQL("select * from parent where student={cardId}").on('cardId -> cardId).as(ParentObj.sample *)

  }
}
