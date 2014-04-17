package controllers

import play.api._
import play.api.mvc._
import com.google.inject.Inject
import service._
import models._
import play.api.libs.json._
import controllers.api.JsonImplicits._

object Application extends Controller {

  @Inject
  val StudentService: StudentService = null

  def index = Action {

    Ok(views.html.index("Your new application is ready."))
  }

  def allStudents = Action { implicit request =>
    val students = StudentService.all

    Ok(Json.obj("students" -> students))
  }

  def saveStudent = Action(parse.json) { implicit request =>
    import play.api.libs.json.Json.fromJson
    import play.api.libs.concurrent.Execution.Implicits._
    val obj = Json.fromJson[Student](request.body).get
    val result = StudentService.save(obj)

    Ok(Json.toJson(result))
  }

  // -- Javascript routing
  def javascriptRoutes = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(
      controllers.routes.javascript.Application.allStudents,
      controllers.routes.javascript.Application.saveStudent
    )).as("text/javascript")
  }

}
