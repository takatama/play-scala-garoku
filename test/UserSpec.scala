import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.User
import anorm.NotAssigned

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {
  "User#authenticate" should {
    "allow to access valid user" in new WithApplication {
      val email = "username@example.com"
      val name = "first family"
      val password = "password"

      User.create(
        User(NotAssigned, email, name, password)
      )

      val user = User.authenticate(email, password)
      user must beSome
      user match {
        case Some(u) => {
            u.email must beEqualTo(email)
            u.name must beEqualTo(name)
	  }
	case None => failure("valid user is not authenticated")
      }
    }
  }

  "User#authenticate" should {
    "not allow to access invalid user" in new WithApplication {
      val email = "username@example.com"
      val password = "password"

      val user = User.authenticate(email, password)
      user must beNone
    }
  }
}
