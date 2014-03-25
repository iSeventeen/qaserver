package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Student(
  id: Pk[Long],
  cardId: Long,
  name: String,
  age: Option[Int],
  gender: Int,
  avatar: Option[String]) {

}

object Student {

  val TABLE_NAME = "student"

  val sample = {
    get[Pk[Long]]("id") ~
      get[Long]("card_id") ~
      get[String]("name") ~
      get[Option[Int]]("age") ~
      get[Int]("gender") ~
      get[Option[String]]("avatar") map {
        case id ~ cardId ~ name ~ age ~ gender ~ avatar => Student(id, cardId, name, age, gender, avatar)
      }
  }

  def all(): List[Student] = DB.withConnection { implicit connection =>
    SQL("select * from student").as(Student.sample *)
  }

  def findByCardId(cardId: Long): Option[Student] = DB.withConnection { implicit connection =>
    SQL("select * from student where card_id={cardId}").on('cardId -> cardId).as(Student.sample.singleOpt)
  }

  def create(cardId: Long, name: String, age: Int, gender: Int, avatar: Option[String]) = DB.withConnection { implicit connection =>
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
      ).executeInsert(Student.sample.singleOpt)
  }

}