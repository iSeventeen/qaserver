package models

import play.api.db._
import play.api.mvc._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Parent(
  id: Pk[Long],
  name: String,
  role: String,
  avatar: Option[String],
  student: Long) {

}

object Parent {

  val TABLE_NAME = "parent"

  val sample = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[String]("role") ~
      get[Option[String]]("avatar") ~
      get[Long]("student") map {
        case id ~ name ~ role ~ avatar ~ student => Parent(id, name, role, avatar, student)
      }
  }

  def findByStudentId(studentId: Long): List[Parent] = DB.withConnection { implicit connection =>
    
   SQL("select * from parent where student={studentId}").on('studentId -> studentId).as(Parent.sample *)
     
  }
}
