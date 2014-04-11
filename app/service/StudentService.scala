package service

import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._
import play.api.Play.current
import play.api.db.DB
import models._
import com.google.inject.Inject
import scala.slick.lifted.TableQuery

trait StudentService {

  def all(): List[Student]

  def findByCardId(cardId: String): (Option[Student], List[Parent])

  def save(student: Student): Int

}

class StudentServiceImpl extends StudentService {

  @Inject
  val ParentService: ParentService = null
  def mgDatabase = Database.forDataSource(DB.getDataSource("default"))
  val students = TableQuery[StudentTable]

  def all(): List[Student] = mgDatabase withSession { implicit session: Session =>
    students.list()
  }

  def findByCardId(cardId: String): (Option[Student], List[Parent]) = mgDatabase withSession { implicit session: Session =>
    val student = students.where(_.cardId === cardId).firstOption
    val parents = student match {
      case Some(value) => ParentService.findByStudent(value.cardId)
      case _ => List[Parent]()
    }
    
    (student, parents)
  }

  def save(student: Student): Int = mgDatabase withSession { implicit session: Session =>
    students.insert(student)
  }
}
