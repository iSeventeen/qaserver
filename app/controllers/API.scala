package controllers

import play.api.mvc.Controller
import com.wordnik.swagger.annotations.Api
import play.api.mvc.Action
import com.wordnik.swagger.annotations.ApiOperation
import play.api.libs.json.Json
import models.HotWords

@Api(value = "/items", listingPath = "/api/docs/items", description = "QA Server API")
object API extends Controller {

  def cachd[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    val maxAge = 60
    action(request).withHeaders(CACHE_CONTROL -> s"private,max-age=$maxAge,s-maxage=$maxAge")

  }

  @ApiOperation(value = "Get hot words", notes = "Returns an array of hot words", responseClass = "String[]", httpMethod = "GET")
  def getHotWords() = Action {
      val words = HotWords.all.map(obj => obj.word)
      Ok(Json.toJson(words))
    }

}
