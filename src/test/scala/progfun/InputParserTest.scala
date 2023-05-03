package progfun

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class InputParserTest extends AnyFunSuite with EitherValues with Matchers {

  private val defaultInputLines = List(
    "4 5",
    "1 2 N",
    "GAGAGAGAA",
    "3 3 E",
    "AADAADADDA"
  )

  private val defaultInput = Input(
    Field.default(),
    List.empty
  )

  test("empty input should be left") {
    val e = InputParser.fromLines(List.empty).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("Empty Input.")
  }

  test("should get limits of the field") {
    InputParser.fromLines(defaultInputLines) match {
      case Right(setUp) => {
        assert(setUp.field.width === 5)
        assert(setUp.field.height === 6)
      }
      case Left(e) => assert(e.getMessage === "")
    }
  }

  test("should get left when limits are not separated by a space") {
    val e = InputParser.fromLines(defaultInputLines.updated(0, "54")).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("54 is not parsable to field limit.")
  }

  test("should get left when limits are not ints") {
    val e = InputParser.fromLines(defaultInputLines.updated(0, "5 A")).left.value
    e shouldBe a [DonneesIncorrectesException]
    e.getMessage should be ("5 A is not parsable to field limit.")
  }

  test("should parse lawnmowers start position") {
    val setUp = InputParser.fromLines(defaultInputLines)
    val lawnMowers = setUp.getOrElse(defaultInput).lawnMowers
    assert(lawnMowers(0).position.equals(Position(1,2)))
    assert(lawnMowers(1).position.equals(Position(3,3)))
  }

  test("should parse lawnmowers start orientations") {
    val fourOrientations = defaultInputLines.concat(List(
      "2 3 W",
      "G",
      "4 5 S",
      "A"
    ))
    val setUp = InputParser.fromLines(fourOrientations)
    val lawnMowers = setUp.getOrElse(defaultInput).lawnMowers
    assert(lawnMowers(0).orientation.equals(North))
    assert(lawnMowers(1).orientation.equals(East))
    assert(lawnMowers(2).orientation.equals(West))
    assert(lawnMowers(3).orientation.equals(South))
  }

  test("should get left with invalid orientation") {
    val e = InputParser.fromLines(defaultInputLines.updated(1, "1 2 A")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[A] is not an orientation. Accepted values are (N,E,W,S).")
  }

  test("should get left with missing last line") {
    val e = InputParser.fromLines(defaultInputLines.dropRight(1)).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("Missing line. Each lawnmower should be defined by a first line for the start position and orientation, and a second line for the instructions.")
  }

  test("should get left with mal formatted line") {
    val e = InputParser.fromLines(defaultInputLines.updated(1, "12N")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("12N is not parsable to position and orientation. Expected format like 'x y z'.")
  }

  test("should get left with non integer values for position") {
    val e = InputParser.fromLines(defaultInputLines.updated(1, "1 B N")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[1,B] not parsable to a position.")
  }

  test("should parse lawnmowers instructions") {
    val setUp = InputParser.fromLines(defaultInputLines)
    val lawnMowers = setUp.getOrElse(defaultInput).lawnMowers
    val expected = List(RotateLeft, MoveForward, RotateLeft, MoveForward, RotateLeft, MoveForward, RotateLeft, MoveForward, MoveForward)
    lawnMowers(0).instructions shouldBe expected
  }

  test("should get left with unknown instruction") {
    val e = InputParser.fromLines(defaultInputLines.updated(2, "AGZB")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[Z] is not an instruction. Accepted values are (G,D,A).")
  }
}