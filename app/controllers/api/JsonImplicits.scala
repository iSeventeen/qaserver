package controllers.api

import anorm._
import models._
import play.api.libs.json._
import org.joda.time.DateTime

object JsonImplicits {

  /*
  implicit val studentWrites = new Writes[StudentObj] {
    def writes(s: StudentObj): JsValue = {
      Json.obj(
        "id" -> s.id, "cardId" -> s.cardId, "name" -> s.name, "age" -> s.age, "gender" -> s.gender, "avatar" -> s.avatar,
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
  * 
  */

  /**
  implicit object StudentObjFormat extends Format[StudentObj] {
    def writes(s: StudentObj) = {
      Json.obj("id" -> s.id, "cardId" -> s.cardId, "name" -> s.name, "age" -> s.age, "gender" -> s.gender, "avatar" -> s.avatar,
        "createdAt" -> s.createdAt, "updatedAt" -> s.updatedAt, "parents" -> Json.toJson(s.parents))
    }

    def reads(json: JsValue) = {
      JsSuccess(StudentObj(
        -1,
        (json \ "cardId").as[String],
        (json \ "name").as[String],
        (json \ "age").as[Option[Int]],
        (json \ "gender").as[Int],
        (json \ "avatar").as[Option[String]],
        (json \ "createdAt").as[Option[Date]],
        (json \ "updatedAt").as[Option[Date]]
      ))
    }
  }

  implicit val parentWrites = new Writes[ParentObj] {
    def writes(p: ParentObj): JsValue = {
      Json.obj(
        "id" -> p.id.get, "name" -> p.name, "role" -> p.role, "avatar" -> p.avatar, "student" -> p.student
      )
    }
  }
  * 
  */
  
    implicit val idsWrites = new Writes[(Option[Long], Option[DateTime])] {
      def writes(value: (Option[Long], Option[DateTime])): JsValue = {
        Json.obj("id" -> value._1,
          "updatedAt" -> value._2)
      }
    }

  implicit val studentFormat = Json.format[Student]

  implicit val parentFormat = Json.format[Parent]

}