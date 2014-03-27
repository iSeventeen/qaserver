package controllers.api

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiErrors
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import controllers.api.JsonImplicits._
import javax.ws.rs.PathParam
import models._
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import com.wordnik.swagger.annotations.ApiError
import service.CardIdError

@Api(value = "/garten", listingPath = "/api/docs/garten", description = "Kindergarten Server API")
object GAPI extends Controller {

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")

  }

  @ApiOperation(value = "Find student by card_id", notes = "Returns a student", responseClass = "Student", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid card id supplied"), new ApiError(code = 404, reason = "Student not found")))
  def getStudent(@ApiParam(value = "Student Card ID")@PathParam("cardId") cardId: String) = Action {
    val result = Student.findByCardId(cardId)

    result match {
      case (Some(student), parents) => {
        student.parents = parents
        Ok(Json.toJson(student))
      }
      case _ => Ok(Json.obj("status" -> "error", "message" -> "Student not found"))
    }
  }

  @ApiOperation(value = "Find parents by student id", notes = "Returns a arry of parents", responseClass = "Parent", httpMethod = "GET")
  @ApiErrors(Array(new ApiError(code = 400, reason = "Invalid student id supplied"), new ApiError(code = 404, reason = "Parents not found")))
  def getParents(@ApiParam(value = "Student ID")@PathParam("studentId") studentId: Long) = Action {
    val parents = Parent.findByStudentId(studentId)
    if(null == parents || 0 ==parents.length) {
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
      val student = Student.create(cardId, name, age, gender, avatar)
      student match {
        case Some(obj) => Ok(Json.obj("status" -> "OK", "cardId" -> obj.cardId))
        case None => Ok(Json.obj("status" -> "error", "message" -> "create student failed"))
      }

    } catch {
      case e: CardIdError => {
        BadRequest(Json.obj("status" -> "error", "message" -> "This card id not available."))
      }
    }
  }

}
