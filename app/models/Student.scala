package models

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.Tag
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date
import org.joda.time.DateTime
import play.api.Logger

case class Student(
  id: Option[Long] = None,
  cardId: String,
  name: String,
  age: Option[Int],
  gender: Int,
  avatar: Option[String],
  cratedAt: Option[DateTime],
  updatedAt: Option[DateTime]) {

  var parents: List[Parent] = Nil
}

class StudentTable(tag: Tag) extends Table[Student](tag, "student") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cardId = column[String]("card_id", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def age = column[Int]("age")
  def gender = column[Int]("gender")
  def avatar = column[String]("avatar")
  def createdAt = column[DateTime]("created_at")
  def updatedAt = column[DateTime]("updated_at")

  def * = (id.?, cardId, name, age.?, gender, avatar.?, createdAt.?, updatedAt.?) <> (Student.tupled, Student.unapply _)
}

case class StudentObj(
  id: Pk[Long],
  cardId: String,
  name: String,
  age: Option[Int],
  gender: Int,
  avatar: Option[String],
  createdAt: Option[Date],
  updatedAt: Option[Date]) {

  var parents: List[ParentObj] = Nil
}

object StudentObj {

  val TABLE_NAME = "student"

  val sample = {
    get[Pk[Long]]("id") ~
      get[String]("card_id") ~
      get[String]("name") ~
      get[Option[Int]]("age") ~
      get[Int]("gender") ~
      get[Option[String]]("avatar") ~
      get[Option[Date]]("created_at") ~
      get[Option[Date]]("updated_at") map {
        case id ~ cardId ~ name ~ age ~ gender ~ avatar ~ createdAt ~ updatedAt => StudentObj(id, cardId, name, age, gender, avatar, createdAt, updatedAt)
      }
  }

  def all(): List[StudentObj] = DB.withConnection { implicit connection =>
    SQL("select * from student").as(StudentObj.sample *)
  }

  def findByCardId(cardId: String): (Option[StudentObj], List[ParentObj]) = DB.withConnection { implicit connection =>
    val studentOpt = SQL("select * from student where card_id={cardId}").on('cardId -> cardId).as(StudentObj.sample.singleOpt)

    val parents = studentOpt match {
      case Some(student) => {
        SQL("select * from parent where student={cardId}").on('cardId -> student.cardId).as(ParentObj.sample *)
      }

      case None => List[ParentObj]()
    }

    (studentOpt, parents)
  }
  
  def findByCardIds(cardIds: String): List[StudentObj] = DB.withConnection { implicit connection =>
    Logger.info("...cardIds="+cardIds)
    val ids = cardIds.split(",").toList.map("'"+ _ +"'").mkString("(", ",", ")")
    SQL("select * from student where card_id in ({ids})").on('ids -> ids).as(StudentObj.sample *)
  }

  def findUpdateStudents(values: List[(String, Date)]): (List[StudentObj], List[StudentObj]) = {
    val ids = values.map(v => v._1)
    val allStudents = all()
    val newStudents = allStudents.filterNot(p => ids.contains(p.cardId))
    val oldStudents = allStudents.filter(p => ids.contains(p.cardId))
    val oldStudentIds = oldStudents.map(value => (value.cardId, value.updatedAt))
    val updatedIds = oldStudentIds.filterNot(p => values.contains(p)).map(v => v._1)
    val updatedStudents = oldStudents.filter(p => updatedIds.contains(p.cardId))
        
    (newStudents, updatedStudents)
    
  }

  def create(cardId: String, name: String, age: Int, gender: Int, avatar: Option[String]) = DB.withConnection { implicit connection =>
    SQL(
      """
      insert into student(card_id, name, age, gender, avatar) values (
        {cardId}, {name}, {age}, {gender}, {avatar}
        )
      """
    ).on(
        'cardId -> cardId,
        'name -> name,
        'age -> age,
        'gender -> gender,
        'avatar -> avatar
      ).executeInsert(StudentObj.sample.singleOpt)
  }

}
