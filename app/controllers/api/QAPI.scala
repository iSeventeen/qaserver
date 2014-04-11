package controllers.api

import play.api.mvc.Controller
import com.wordnik.swagger.annotations.Api
import play.api.mvc.Action
import com.wordnik.swagger.annotations.ApiOperation
import play.api.libs.json.Json
import models.HotWords
import com.wordnik.swagger.annotations.ApiParam
import javax.ws.rs.PathParam
import com.google.inject.Inject
import service.HotWordService

@Api(value = "/qa", listingPath = "/api/docs/qa", description = "QA Server API")
object QAPI extends Controller {

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")

  }

  @Inject
  val HotwordService: HotWordService = null

  @ApiOperation(value = "Get hot words", notes = "Returns an array of hot words", responseClass = "String[]", httpMethod = "GET")
  def getHotWords() = Action {
    val words = HotwordService.findAll.map(obj => obj.word)
    Ok(Json.toJson(words))
  }

  @ApiOperation(value = "get a hot word by id", notes = "Returns a hot word", responseClass = "String", httpMethod = "GET")
  def gethotWord(@ApiParam(value = "ID of the hot word return")@PathParam("id") id: Long) = Action {
    val hotword = HotwordService.findById(id)

    if (hotword.isDefined) {
      Ok(Json.obj("status" -> "error", "message" -> "The id is no available."))
    } else {
      Ok(Json.obj("status" -> "OK", "word" -> hotword.get.word))
    }

  }

}
