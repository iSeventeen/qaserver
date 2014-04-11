package controllers.api

import com.google.inject.Inject
import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiError
import com.wordnik.swagger.annotations.ApiErrors
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import controllers.api.JsonImplicits._
import javax.ws.rs.PathParam
import models.Student
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import service.CardIdError
import service.ParentService
import service.StudentService
import models.StudentObj
import models.ParentObj
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import org.joda.time.DateTime
import java.util.Date
import play.api.Logger

@Api(value = "/garten", listingPath = "/api/docs/garten", description = "Kindergarten Server API")
object GAPI extends Controller {

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")
  }

  @ApiOperation(value = "Find all the students", notes = "Returns a array of students", responseClass = "Student[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = ""), new ApiError(code = 404, reason = "Students not found")))
  def getStudents() = Action {
    val students = StudentObj.all
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      Ok(Json.toJson(students))
    }
  }

/*
  @ApiOperation(value = "Find the students by cardIds", notes = "Returns a array of students", responseClass = "Student[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid cardIds"), new ApiError(code = 404, reason = "Students not found")))
  def getStudentsByIds(
    @ApiParam(value = "The array string of Student Card ID")@PathParam("cardIds") newCardIds: String,
    @ApiParam(value = "The array string of Student Card ID")@PathParam("cardIds") updateCardIds: String) = Action {
    
    val students = StudentObj.all
    val newIds = newCardIds.split(",").toList
    val updateIds = updateCardIds.split(",").toList
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      val newItems = students.filter(p => newIds.contains(p.cardId))
      Logger.info(">>>newItems:"+newItems)
      val updateItems = students.filter(p => updateIds.contains(p.cardId))
      Logger.info(">>>updateItems:"+updateItems)
      Ok(Json.obj("newStudents" -> Json.toJson(newItems), "updateStudents" -> Json.toJson(updateItems)))
    }
  }
  */

  @ApiOperation(value = "Find the students by cardIds", notes = "Returns a array of students", responseClass = "Student[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid cardIds"), new ApiError(code = 404, reason = "Students not found")))
  def getStudentsByIds(@ApiParam(value = "The array string of Student Card ID")@PathParam("cardIds") cardIds: String) = Action {
    
    val students = StudentObj.all
    val ids = cardIds.split(",").toList
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      val objs = students.filter(p => ids.contains(p.cardId))
      Ok(Json.toJson(objs))
    }
  }

  @ApiOperation(value = "Find all cardId and updatedAt of student", notes = "Returns a array of Tuple contains cardId and updateAt", responseClass = "List[(String, Date)]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 404, reason = "not found result")))
  def getStudentIds() = Action {

    implicit val idsWrites = new Writes[(String, Option[Date])] {
      def writes(value: (String, Option[Date])): JsValue = {
        Json.obj("cardId" -> value._1,
          "updatedAt" -> value._2)
      }
    }
    val students = StudentObj.all
    if (null == students || 0 == students.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    } else {
      val result = students.map(student => (student.cardId, student.updatedAt))
      Ok(Json.toJson(result))
    }
  }

  @ApiOperation(value = "Find student by card_id", notes = "Returns a student", responseClass = "Student", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid card id supplied"), new ApiError(code = 404, reason = "Student not found")))
  def getStudent(
    @ApiParam(value = "Student Card ID")@PathParam("cardId") cardId: String) = Action {
    val result = StudentObj.findByCardId(cardId)

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
    val parents = ParentObj.findAll

    if (null == parents || 0 == parents.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Parents not found"))
    } else {
      Ok(Json.toJson(parents))
    }
  }

  @ApiOperation(value = "Find parents by card id", notes = "Returns a array of parents", responseClass = "Parent[]", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid student card id supplied"), new ApiError(code = 404, reason = "Parents not found")))
  def getParentsById(@ApiParam(value = "Student Card ID")@PathParam("cardId") cardId: String) = Action {
    val parents = ParentObj.findByStudent(cardId)

    if (null == parents || 0 == parents.length) {
      Ok(Json.obj("status" -> "error", "message" -> "Parent not found"))
    } else {
      Ok(Json.toJson(parents))
    }
  }

  @ApiOperation(value = "Create student", notes = "Returns Student", responseClass = "Student", httpMethod = "POST")
  def createStudent(
    @ApiParam(value = "cardId")@PathParam("cardId") cardId: String,
    @ApiParam(value = "name")@PathParam("name") name: String,
    @ApiParam(value = "age")@PathParam("age") age: Int,
    @ApiParam(value = "gender")@PathParam("gender") gender: Int,
    @ApiParam(value = "avatar")@PathParam("avatar") avatar: Option[String]) = Action {

    try {
      val result = StudentService.save(Student(None, cardId, name, Some(age), gender, avatar, None, None))
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

}
