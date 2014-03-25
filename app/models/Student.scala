package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class User(
  userId: Pk[Long],
  cardId: Long,
  name: String,
  age: Option[Int],
  gender: Int,
  avatar: Option[String]) {

}

object User {

  val TABLE_NAME = "user"

  val sample = {
    get[Pk[Long]]("id") ~
      get[Long]("card_id") ~
      get[String]("name") ~
      get[Option[Int]]("age") ~
      get[Int]("gender") ~
      get[Option[String]]("avatar") map {
        case userId ~ cardId ~ name ~ age ~ gender ~ avatar => User(userId, cardId, name, age, gender, avatar)
      }
  }

  def all(): List[User] = DB.withConnection { implicit connection =>
    SQL("select * from user").as(User.sample *)
  }

  def findByCardId(cardId: Long): Option[User] = DB.withConnection { implicit connection =>
    SQL("select * from user where card_id={cardId}").on('cardId -> cardId).as(User.sample.singleOpt)
  }

  def save(user: User) = DB.withConnection { implicit connection =>
    SQL(
      """
      insert into user(card_id, name, age, gender, avatar) values (
        {cardId}, {name}, {age}, {gender}, {avatar}
        )
      """
    ).on(
        'cardId -> user.cardId,
        'name -> user.name,
        'age -> user.age,
        'gender -> user.gender,
        'avatar -> user.avatar
      ).executeInsert(User.sample.singleOpt)
  }

}