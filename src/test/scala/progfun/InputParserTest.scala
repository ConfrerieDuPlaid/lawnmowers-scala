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

  private val defaultSetUp = SetUp(
    Field.default(),
    List.empty
  )

  test("empty input should be left") {
    val e = InputParser.fromLines(List.empty).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("Empty Input.")
  }

  test("should get limits of the field") {
    val setUp = InputParser.fromLines(defaultInput)
    val field = setUp.getOrElse(defaultSetUp).field
    assert(field.width === 5)
    assert(field.height === 6)
  }

  test("should get left when limits are not separated by a space") {
    val e = InputParser.fromLines(defaultInput.updated(0, "54")).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("54 is not parsable to field limit.")
  }

  test("should get left when limits are not ints") {
    val e = InputParser.fromLines(defaultInput.updated(0, "5 A")).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("5 A is not parsable to field limit.")
  }

  test("should parse lawnmowers start position") {
    val setUp = InputParser.fromLines(defaultInput)
    val lawnMowers = setUp.getOrElse(defaultSetUp).lawnMowers
    assert(lawnMowers(0).start.position.equals(Position(1,2)))
    assert(lawnMowers(1).start.position.equals(Position(3,3)))
  }

  test("should parse lawnmowers start orientations") {
    val setUp = InputParser.fromLines(defaultInput)
    val lawnMowers = setUp.getOrElse(defaultSetUp).lawnMowers
    assert(lawnMowers(0).start.orientation.equals(North))
    assert(lawnMowers(1).start.orientation.equals(East))
  }
}