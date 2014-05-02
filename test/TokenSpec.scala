import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Token

@RunWith(classOf[JUnitRunner])
class TokenSpec extends Specification {
  "Token#create" should {
    "create another token with the same email" in new WithApplication {
      val email = "username@example.com"

      val token1 = Token.create(email)
      token1.email must beEqualTo(email)

      val token2 = Token.create(email)
      token2.email must beEqualTo(email)

      token1.token must not(beEqualTo(token2.token))
    }
  }

  "Token#created" should {
    "set created timestamp" in new WithApplication {
      val email = "username@example.com"
      val token = Token.create(email)
      token.created must not beNull
    }
  }

  "Token#findByToken" should {
    "find a token" in new WithApplication {
      val email = "username@example.com"
      val token = Token.create(email)
      Token.findByToken(token.token) match {
        case Some(t) => 
          t.token must beEqualTo(token.token)
          t.email must beEqualTo(token.email)
	case None => failure("must be found")
      }
    }
  }

}
