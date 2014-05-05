package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Image(id: Pk[Long], contentType: String, path: String, created: Date)

object Image {
  val simple = {
    get[Pk[Long]]("image.id") ~
    get[String]("image.content_type") ~
    get[String]("image.path") ~
    get[Date]("image.created") map {
      case id~contentType~path~created => Image(id, contentType, path, created)
    }
  }

  def create(contentType: String, path: String): Image = {
    val created = new Date
    DB.withConnection { implicit connection =>
      val id: Long = SQL("select next value for image_seq").as(scalar[Long].single)
      val image = Image(Id(id), contentType, path, created)

      SQL(
        """
	  insert into image
	  values ({id}, {contentType}, {path}, {created})
	"""
      ).on(
        "id" -> image.id,
        "contentType" -> image.contentType,
        "path" -> image.path,
	"created" -> image.created
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
}

