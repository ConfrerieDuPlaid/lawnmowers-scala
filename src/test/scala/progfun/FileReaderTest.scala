package progfun

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

@SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
class FileReaderTest extends AnyFunSuite with Matchers {
  test("should get file") {
    val expected = """5 5
                     |1 2 N
                     |GAGAGAGAA
                     |3 3 E
                     |AADAADADDA""".stripMargin
    FileReader.read("src/test/resources/progfun/input.test").get should be (expected)
  }

  test("should not get non existing file") {
    FileReader.read(" non_existing.file ") should be(None)
  }

  test("and a directory ?") {
    FileReader.read("src") should be(None)
  }
}
