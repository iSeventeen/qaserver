package controllers.api

import com.google.inject.Inject
import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiError
import com.wordnik.swagger.annotations.ApiErrors
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import controllers.api.JsonImplicits._
import play.api.libs.concurrent.Execution.Implicits._
import javax.ws.rs.PathParam
import models.Student
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import service.CardIdError
import service.ParentService
import service.StudentService
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import org.joda.time.DateTime
import java.util.Date
import play.api.Logger
import service.StudentService
import service.ParentService

@Api(value = "/garten", listingPath = "/api/docs/garten", description = "Kindergarten Server API")
object GAPI extends Controller {

  @Inject
  val StudentService: StudentService = null
  @Inject
  val ParentService: ParentService = null

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")
  }

  @ApiOperation(value = "Find all the students", notes = "Returns a array of students", responseClass = "Student[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = ""), new ApiError(code = 404, reason = "Students not found")))
  def getStudents() = Action {
    val students = StudentService.all
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      Ok(Json.toJson(students))
    }
  }

  @ApiOperation(value = "Find the students by IDs", notes = "Returns a array of students", responseClass = "Student[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid IDs"), new ApiError(code = 404, reason = "Students not found")))
  def getStudentsByIds(@ApiParam(value = "The array string of Student ID")@PathParam("IDs") ids: String) = Action {

    val students = StudentService.all
    val idList = ids.split(",").toList
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      val objs = students.filter(p => idList.contains(p.id))
      Ok(Json.toJson(objs))
    }
  }

  @ApiOperation(value = "Find all cardId and updatedAt of student", notes = "Returns a array of Tuple contains cardId and updateAt", responseClass = "List[(String, Date)]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 404, reason = "not found result")))
  def getStudentIds() = Action {

    implicit val idsWrites = new Writes[(Option[Long], Option[DateTime])] {
      def writes(value: (Option[Long], Option[DateTime])): JsValue = {
        Json.obj("id" -> value._1,
          "updatedAt" -> value._2)
      }
    }
    val students = StudentService.all
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      val result = students.map(student => (student.id, student.updatedAt))
      Ok(Json.toJson(result))
    }
  }

  @ApiOperation(value = "Find student by id", notes = "Returns a student", responseClass = "Student", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid id supplied"), new ApiError(code = 404, reason = "Student not found")))
  def getStudent(
    @ApiParam(value = "Student ID")@PathParam("id") id: Long) = Action {
    val result = StudentService.findById(id)

    result match {
      case (Some(student), parents) => {
        student.parents = parents
        Ok(Json.toJson(student))
      }
      case _ => Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    }
  }

  @ApiOperation(value = "Find all Parents", notes = "Returns a Parents", responseClass = "Parent[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 404, reason = "Parents not found")))
  def getParents() = Action {
    val parents = ParentService.all

    if (null == parents || 0 == parents.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Parents not found"))
    } else {
      Ok(Json.toJson(parents))
    }
  }

  @ApiOperation(value = "Find parents by student id", notes = "Returns a array of parents", responseClass = "Parent[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid student id supplied"), new ApiError(code = 404, reason = "Parents not found")))
  def getParentsById(@ApiParam(value = "Student ID")@PathParam("id") studentId: Long) = Action {
    val parents = ParentService.findByStudent(studentId)

    if (null == parents || 0 == parents.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Parent not found"))
    } else {
      Ok(Json.toJson(parents))
    }
  }

  /*
  @ApiOperation(value = "Create student", notes = "Returns Student", responseClass = "Student", httpMethod = "POST")
  def createStudent(
    @ApiParam(value = "cardId")@PathParam("cardId") cardId: Option[String],
    @ApiParam(value = "name")@PathParam("name") name: String,
    @ApiParam(value = "gender")@PathParam("gender") gender: Int,
    @ApiParam(value = "grade")@PathParam("grade") grade: Int,
    @ApiParam(value = "address")@PathParam("address") address: String,
    @ApiParam(value = "avatar")@PathParam("avatar") avatar: Option[String],
    @ApiParam(value = "note")@PathParam("note") note: Option[String]) = Action {

    val currDate = Some(DateTime.now())

    val student = Student(None, cardId, name, gender, grade, address, avatar, note, currDate, currDate)
    try {
      val result = StudentService.save(student)
      result match {
        case 1 => Ok(Json.obj("status" -> "OK", "message" -> "save student success"))
        case _ => Ok(Json.obj("status" -> "error", "message" -> "create student failed"))
      }

    } catch {
      case e: CardIdError => {
        BadRequest(Json.obj("status" -> "error", "message" -> "This card id not available."))
      }
    }
  }
  * 
  */
  @ApiOperation(value = "Create student", notes = "Returns Student", responseClass = "Student", httpMethod = "POST")
  def createStudent() = Action(parse.json) { implicit request =>

    val obj = Json.fromJson[Student](request.body).get
    val result = StudentService.save(obj)
    result match {
      case -1 => Ok(Json.obj("status" -> "error", "message" -> "create student failed"))
      case _ => Ok(Json.toJson(obj.copy(id = Some(result))))
    }
  }
  
}

