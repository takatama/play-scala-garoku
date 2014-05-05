package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Log(id: Pk[Long], imageId: Pk[Long], user: String, created: Date)

object Log {
  val simple = {
    get[Pk[Long]]("log.id") ~
    get[Pk[Long]]("log.image_id") ~
    get[String]("log.user") ~
    get[Date]("log.created") map {
      case id~imageId~user~created => Log(id, imageId, user, created)
    }
  }
}

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

  def addLog(image: Image, user: String): Option[Log] = DB.withConnection { implicit connection =>
    if (image.user == user) {
      None
    } else {
      val id: Long = SQL("select next value for log_seq").as(scalar[Long].single)
      val created = new Date
      val log = Log(Id(id), image.id, user, created)
      SQL("insert into log values({id}, {imageId}, {user}, {created})").on(
        "id" -> log.id,
        "imageId" -> log.imageId,
        "user" -> log.user,
        "created" -> log.created
      ).executeUpdate()
      Some(log)
    }
  }

  def logs(image: Image): List[Log] = DB.withConnection { implicit connection =>
    SQL("select * from log where image_id = {imageId}").on(
      "imageId" -> image.id
    ).as(Log.simple *)
  }
}

