import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Record

@RunWith(classOf[JUnitRunner])
class RecordSpec extends Specification {
  "Record#create" should {
    "create a record with a memo" in new WithApplication {
      Record.all().length must beEqualTo(0)
      val memo = "the memo"
      Record.create(memo).memo must beEqualTo(memo)
      Record.all().length must beEqualTo(1)
    }
  }

  "Record#created" should {
    "set created timestamp" in new WithApplication {
      val memo = "the memo"
      val record = Record.create(memo)
      record.created must not beNull
    }
  }
}
