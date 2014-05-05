import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.{Image, Record}

@RunWith(classOf[JUnitRunner])
class ImageSpec extends Specification {
  "Image#create" should {
    "create an image with contentType and path" in new WithApplication {
      Image.all().length must beEqualTo(0)
      val contentType = "image/png"
      val path = "image.png"
      val user = "user@example.com"
      Image.create(contentType, path, user)
      Image.all().length must beEqualTo(1)
    }
  }

  "Image#created" should {
    "set created timestamp" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user = "user@example.com"
      val image = Image.create(contentType, path, user)
      Image.all().length must beEqualTo(1)
      image.created must not beNull
    }
  }

  "Image#find" should {
    "find an image by id" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user = "user@example.com"
      val id = Image.create(contentType, path, user).id.get
      Image.find(id) match {
        case Some(image) => image.id.get must beEqualTo(id)
	case None => failure("must find an image by id")
      }
    }
  }

  "Image#findByUser" should {
    "find images created by the specified user" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user1 = "user1@example.com"
      Image.create(contentType, path, user1)
      val user2 = "user2@example.com"
      Image.create(contentType, path, user2)
      val images = Image.findByUser(user1)
      images.length must beEqualTo(1)
      images.head.user must beEqualTo(user1) 
    }
  }

  "Image#addLog" should {
    "create a new log" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user1 = "user1@example.com"
      val image = Image.create(contentType, path, user1)
      val user2 = "user2@example.com"
      Image.addLog(image, user2) match {
        case Some(log) => log.user must beEqualTo(user2)
	case None => failure
      }
    }
  }

  "Image#addLog" should {
    "not create a new log of image owner" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user = "user@example.com"
      val image = Image.create(contentType, path, user)
      Image.addLog(image, user) match {
	case Some(_) => failure
        case None => success
      }
    }
  }

  "Image#logs" should {
    "return logs after Image#addLog" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val user1 = "user1@example.com"
      val image = Image.create(contentType, path, user1)
      Image.logs(image).length must beEqualTo(0)
      val user2 = "user2@example.com"
      Image.addLog(image, user2)
      val logs = Image.logs(image)
      logs.length must beEqualTo(1)
      logs.head.user must beEqualTo(user2)
    }
  }
}
