package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import com.github.t3hnar.bcrypt._
import play.api.Logger

case class User(id: Pk[Long], email: String, name: String, password: String)

object User {
  val simple = {
    get[Pk[Long]]("user.id") ~
    get[String]("user.email") ~
    get[String]("user.name") ~
    get[String]("user.password") map {
      case id~email~name~password => User(id, email, name, password)
    }
  }

  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email}").on(
        "email" -> email
      ).as(User.simple.singleOpt)
    }
  }

  def create(user: User): Either[String, User] = {
    findByEmail(user.email) match {
      case Some(_) => Left("The email has been used.")
      case None => {
        DB.withConnection { implicit connection =>
          val id: Long = user.id.getOrElse {
            SQL("select next value for user_seq").as(scalar[Long].single)
          }

          val hashed = user.password.bcrypt(generateSalt)

          SQL(
            """
	      insert into user values (
	        {id}, {email}, {name}, {password}
	      )
	    """
          ).on(
            "id" -> id,
            "email" -> user.email,
	    "name" -> user.name,
	    "password" -> hashed
          ).executeUpdate()

          Right(user.copy(id = Id(id)))
        }
      }
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
	  select * from user where
	  email = {email}
	"""
      ).on(
        "email" -> email
      ).as(User.simple.singleOpt) match {
        case Some(user) => {
          if (password.isBcrypted(user.password)) {
            Some(user)
          } else {
            None
          }
	}
	case None => None
      }
    }
  }
}

