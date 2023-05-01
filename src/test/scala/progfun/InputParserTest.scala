package progfun

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class InputParserTest extends AnyFunSuite with EitherValues with Matchers {

  private val defaultInput = List(
    "4 5",
    "1 2 N",
    "GAGAGAGAA",
    "3 3 E",
    "AADAADADDA"
  )

  private val badSetUp = SetUp(
    Field.default()
  )

  test("empty input should be left") {
    val e = InputParser.fromLines(List.empty).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("Empty Input.")
  }

  test("should get limits of the field") {
    val setUp = InputParser.fromLines(defaultInput)
    val field = setUp.getOrElse(badSetUp).field
    assert(field.width === 4)
    assert(field.height === 5)
  }

  test("should get left when limits are not separated by a space") {
    val e = InputParser.fromLines(defaultInput.updated(0, "54")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("54 is not parsable to field limit.")
  }

  test("should get left when limits are not ints") {
    val e = InputParser.fromLines(defaultInput.updated(0, "5 A")).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("5 A is not parsable to field limit.")
  }
}