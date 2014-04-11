package controllers

import play.api._
import play.api.mvc._
import com.google.inject.Inject
import service._
import models.StudentObj
import models.Student

object Application extends Controller {

  @Inject
  val StudentService: StudentService = null

  def index = Action {

    Ok(views.html.index("Your new application is ready."))
  }

}
