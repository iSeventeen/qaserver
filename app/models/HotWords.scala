package models

import scala.slick.driver.PostgresDriver.simple._

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class HotWords(
  wordId: Long,
  word: String) {

}

object HotWords {
  val TABLE_NAME = "hot_words"

  val sample = {
    get[Long]("word_id") ~
      get[String]("word") map {
        case wordId ~ word => HotWords(wordId, word)
      }
  }

  def all(): Seq[HotWords] = DB.withConnection { implicit connection =>
    SQL("select * from hot_words").as(HotWords.sample *)
  }

}
