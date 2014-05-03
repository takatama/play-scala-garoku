import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.{Image, Record}

@RunWith(classOf[JUnitRunner])
class ImageSpec extends Specification {
  "Image#create" should {
    "create an image with contentType, path associated a record" in new WithApplication {
      Image.all().length must beEqualTo(0)
      val contentType = "image/png"
      val path = "image.png"
      val recordId = Record.create("memo").id
      Image.create(contentType, path, recordId)
      Image.all().length must beEqualTo(1)
    }
  }

  "Image#created" should {
    "set created timestamp" in new WithApplication {
      val contentType = "image/png"
      val path = "image.png"
      val recordId = Record.create("memo").id
      val image = Image.create(contentType, path, recordId)
      Image.all().length must beEqualTo(1)
      image.created must not beNull
    }
  }
}
