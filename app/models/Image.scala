package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Image(id: Pk[Long], contentType: String, path: String, recordId: Pk[Long], created: Date)

object Image {
  val simple = {
    get[Pk[Long]]("image.id") ~
    get[String]("image.content_type") ~
    get[String]("image.path") ~
    get[Pk[Long]]("image.record_id") ~
    get[Date]("image.created") map {
      case id~contentType~path~recordId~created => Image(id, contentType, path, recordId, created)
    }
  }

  def create(contentType: String, path: String, recordId: Pk[Long]): Image = {
    val created = new Date
    DB.withConnection { implicit connection =>
      val id: Long = SQL("select next value for image_seq").as(scalar[Long].single)
      val image = Image(Id(id), contentType, path, recordId, created)

      SQL(
        """
	  insert into image
	  values ({id}, {contentType}, {path}, {recordId}, {created})
	"""
      ).on(
        "id" -> image.id,
        "contentType" -> image.contentType,
        "path" -> image.path,
        "recordId" -> image.recordId,
	"created" -> image.created
      ).executeUpdate()
      image
    }
  }

  def all(): List[Image] = DB.withConnection { implicit connection =>
    SQL("select * from image").as(simple *)
  }
}

