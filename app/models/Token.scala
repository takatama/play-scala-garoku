package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Token(token: String, email: String, created: Date)

object Token {
  val simple = {
    get[String]("token.token") ~
    get[String]("token.email") ~
    get[Date]("token.created") map {
      case token~email~created => Token(token, email, created)
    }
  }

  def findByToken(token: String): Option[Token] = {
    DB.withConnection { implicit connection =>
      SQL("select * from token where token = {token}").on(
        "token" -> token
      ).as(Token.simple.singleOpt)
    }
  }

  def create(email: String): Token = {
    val id = java.util.UUID.randomUUID().toString();
    val created = new Date
    val token = Token(id, email, created)
    DB.withConnection { implicit connection =>
      SQL("insert into token values ({token}, {email}, {created})").on(
        "token" -> token.token,
        "email" -> token.email,
	"created" -> token.created
      ).executeUpdate()
      token
    }
  }

  def all(): List[Token] = DB.withConnection { implicit c =>
    SQL("select * from token").as(simple *)
  }
}
