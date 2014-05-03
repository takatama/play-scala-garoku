package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Record(id: Pk[Long], memo: String, created: Date)

object Record {
  val simple = {
    get[Pk[Long]]("record.id") ~
    get[String]("record.memo") ~
    get[Date]("record.created") map {
      case id~memo~created => Record(id, memo, created)
    }
  }

  def create(memo: String): Record = {
    val created = new Date
    DB.withConnection { implicit connection =>
      val id: Long = SQL("select next value for record_seq").as(scalar[Long].single)
      val record = Record(Id(id), memo, created)

      SQL("insert into record values ({id}, {memo}, {created})").on(
        "id" -> record.id,
        "memo" -> record.memo,
	"created" -> record.created
      ).executeUpdate()
      record
    }
  }

  def all(): List[Record] = DB.withConnection { implicit connection =>
    SQL("select * from record").as(simple *)
  }
}

