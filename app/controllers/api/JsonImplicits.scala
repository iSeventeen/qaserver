package controllers.api

import models._
import play.api.libs.json._

object JsonImplicits {

  implicit val studentWrites = new Writes[StudentObj] {
    def writes(s: StudentObj): JsValue = {
      Json.obj(
        "id" -> s.id.get, "cardId" -> s.cardId, "name" -> s.name, "age" -> s.age, "gender" -> s.gender, "avatar" -> s.avatar,
        "createdAt" -> s.createdAt, "updatedAt" -> s.updatedAt, "parents" -> Json.toJson(s.parents)
      )
    }

    implicit val parentWrites = new Writes[ParentObj] {
      def writes(p: ParentObj): JsValue = {
        Json.obj(
          "id" -> p.id.get, "name" -> p.name, "role" -> p.role, "avatar" -> p.avatar, "student" -> p.student
        )
      }
    }
  }

  implicit val parentWrites = new Writes[ParentObj] {
    def writes(p: ParentObj): JsValue = {
      Json.obj(
        "id" -> p.id.get, "name" -> p.name, "role" -> p.role, "avatar" -> p.avatar, "student" -> p.student
      )
    }
  }

  implicit val studentFormat = Json.format[Student]

  implicit val parentFormat = Json.format[Parent]

}