/*
package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class HotWords(
  id: Pk[Long],
  word: String) {

}

object HotWords {
  val TABLE_NAME = "hot_words"

  val sample = {
    get[Pk[Long]]("id") ~
      get[String]("word") map {
        case id ~ word => HotWords(id, word)
      }
  }

  def all(): Seq[HotWords] = DB.withConnection { implicit connection =>
    SQL("select * from hot_words").as(HotWords.sample *)
  }

  def findById(id: Long): Option[HotWords] = DB.withConnection { implicit connection =>
    SQL("select * from hot_words where id = {id}").on('id -> id).as(HotWords.sample.singleOpt)
  }

}
* 
*/

package models

import play.api.db._
import play.api.Play.current


import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._
import scala.slick.lifted.Tag
import org.joda.time.DateTime

case class HotWords(
  id: Option[Long],
  word: String) {

}

class HotWordsTable(tag: Tag) extends Table[HotWords](tag, "hotwords") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def word = column[String]("word")
  
  def * = (id.?, word) <> (HotWords.tupled, HotWords.unapply _)
}
