package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Image(id: Pk[Long], contentType: String, path: String, created: Date, user: String)

object Image {
  val simple = {
    get[Pk[Long]]("image.id") ~
    get[String]("image.content_type") ~
    get[String]("image.path") ~
    get[Date]("image.created") ~
    get[String]("image.user") map {
      case id~contentType~path~created~user => Image(id, contentType, path, created, user)
    }
  }

  def create(contentType: String, path: String, user: String): Image = {
    val created = new Date
    DB.withConnection { implicit connection =>
      val id: Long = SQL("select next value for image_seq").as(scalar[Long].single)
      val image = Image(Id(id), contentType, path, created, user)

      SQL(
        """
	  insert into image
	  values ({id}, {contentType}, {path}, {created}, {user})
	"""
      ).on(
        "id" -> image.id,
        "contentType" -> image.contentType,
        "path" -> image.path,
	"created" -> image.created,
        "user" -> image.user
      ).executeUpdate()
      image
    }
  }

  def all(): List[Image] = DB.withConnection { implicit connection =>
    SQL("select * from image").as(simple *)
  }

  def find(id: Long): Option[Image] = DB.withConnection { implicit connection =>
    SQL("select * from image where id = {id}").on(
      "id" -> id
    ).as(Image.simple.singleOpt)
  }

  def findByUser(user: String): List[Image] = DB.withConnection { implicit connection =>
    SQL("select * from image where user = {user}").on(
      "user" -> user
    ).as(simple *)
  }
}

