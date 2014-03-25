package controllers.api

import models.Student
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.Json

object JsonImplicits {

  implicit val studentWrites = new Writes[Student] {
    def writes(s: Student): JsValue = {
      Json.obj(
          "id" -> s.id.get
          , "cardId" -> s.cardId
          , "name" -> s.name
          , "age" -> s.age
          , "gender" -> s.gender
          , "avatar" -> s.avatar
      )
    }
  }
}