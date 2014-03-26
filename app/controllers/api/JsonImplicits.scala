package controllers.api

import models.Student
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import models.Parent

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

  implicit val parentWrites = new Writes[Parent] {
    def writes(p: Parent): JsValue = {
      Json.obj(
        "id" -> p.id.get
        , "name" -> p.name
        , "role" -> p.role
        , "avatar" -> p.avatar
        , "student" -> p.student
      )
    }
  }
}