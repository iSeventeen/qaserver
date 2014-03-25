package controllers.api

import play.api.mvc.Controller
import com.wordnik.swagger.annotations.Api
import play.api.mvc.Action
import com.wordnik.swagger.annotations.ApiOperation
import play.api.libs.json.Json
import models.HotWords
import com.wordnik.swagger.annotations.ApiParam
import javax.ws.rs.PathParam

@Api(value = "/qa", listingPath = "/api/docs/qa", description = "QA Server API")
object QAPI extends Controller {

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")

  }

  @ApiOperation(value = "Get hot words", notes = "Returns an array of hot words", responseClass = "String[]", httpMethod = "GET")
  def getHotWords() = Action {
    val words = HotWords.all.map(obj => obj.word)
    Ok(Json.toJson(words))
  }

  @ApiOperation(value = "get a hot word by id", notes = "Returns a hot word", responseClass = "String", httpMethod = "GET")
  def gethotWord(@ApiParam(value = "id")@PathParam("id") id: Long) = Action {

    HotWords.findById(id) match {
      case Some(value) => Ok(Json.obj("status" -> "OK", "word" -> value.word))
      case None => Ok(Json.obj("status" -> "error", "message" -> "The id is no available."))
    }

  }

}
