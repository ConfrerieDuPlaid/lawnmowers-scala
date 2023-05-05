package progfun.in

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import progfun.domain.models._
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.MowLawnCommand

class MowLawnCommandParserTest extends AnyFunSuite with EitherValues with Matchers {

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getInputOrElseThrow(either: Either[Exception, MowLawnCommand]): MowLawnCommand = {
    either match {
      case Right(input)    => input
      case Left(exception) => throw exception
    }
  }

  private val defaultInputLines = List(
    "4 5",
    "1 2 N",
    "GAGAGAGAA",
    "3 3 E",
    "AADAADADDA"
  )

  test("empty input should be left") {
    val e = InputParser.fromLines(List.empty).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("Empty Input.")
  }

  test("should get limits of the field") {
    val input = getInputOrElseThrow(InputParser.fromLines(defaultInputLines))
    assert(input.lawn.width === 5)
    assert(input.lawn.height === 6)
  }

  test("should get left when limits are not separated by a space") {
    val e = InputParser.fromLines(defaultInputLines.updated(0, "54")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("54 is not parsable to field limit.")
  }

  test("should get left when limits are not ints") {
    val e =
      InputParser.fromLines(defaultInputLines.updated(0, "5 A")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("5 A is not parsable to field limit.")
  }

  test("should parse lawnmowers start position") {
    val lawnMowers = getInputOrElseThrow(
      InputParser.fromLines(defaultInputLines)
    ).mowers.map(l => l.lawnmower)
    assert(lawnMowers(0).position.equals(Position(1, 2)))
    assert(lawnMowers(1).position.equals(Position(3, 3)))
  }

  test("should parse lawnmowers start orientations") {
    val fourOrientations = defaultInputLines.concat(
      List(
        "2 3 W",
        "G",
        "4 5 S",
        "A"
      )
    )

    val lawnMowers = getInputOrElseThrow(
      InputParser.fromLines(fourOrientations)
    ).mowers.map(l => l.lawnmower)
    assertResult((North, East, West, South)) {
      (
        lawnMowers(0).orientation,
        lawnMowers(1).orientation,
        lawnMowers(2).orientation,
        lawnMowers(3).orientation
      )
    }
  }

  test("should get left with invalid orientation") {
    val e =
      InputParser.fromLines(defaultInputLines.updated(1, "1 2 A")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be(
      "[A] is not an orientation. Accepted values are (N,E,W,S)."
    )
  }

  test("should get left with missing last line") {
    val e = InputParser.fromLines(defaultInputLines.dropRight(1)).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be(
      "Missing line. Each lawnmower should be defined by a first line for the start position and orientation, and a second line for the instructions."
    )
  }

  test("should get left with mal formatted line") {
    val e =
      InputParser.fromLines(defaultInputLines.updated(1, "12N")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be(
      "12N is not parsable to position and orientation. Expected format like 'x y z'."
    )
  }

  test("should get left with non integer values for position") {
    val e =
      InputParser.fromLines(defaultInputLines.updated(1, "1 B N")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[1,B] not parsable to a position.")
  }

  test("should parse first lawnmower instructions") {
    val lawnMowers =
      getInputOrElseThrow(InputParser.fromLines(defaultInputLines)).mowers
    lawnMowers(0).instructions shouldBe List(
      RotateLeft,
      MoveForward,
      RotateLeft,
      MoveForward,
      RotateLeft,
      MoveForward,
      RotateLeft,
      MoveForward,
      MoveForward
    )
  }

  test("should parse second lawnmower instructions") {
    val lawnMowers =
      getInputOrElseThrow(InputParser.fromLines(defaultInputLines)).mowers
    lawnMowers(1).instructions shouldBe List(
      MoveForward,
      MoveForward,
      RotateRight,
      MoveForward,
      MoveForward,
      RotateRight,
      MoveForward,
      RotateRight,
      RotateRight,
      MoveForward
    )
  }

  test("should get left with unknown instruction") {
    val e =
      InputParser.fromLines(defaultInputLines.updated(2, "AGZB")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be(
      "[Z] is not an instruction. Accepted values are (G,D,A)."
    )
  }

  test("should get empty list of lawnmowers") {
    val lawnmowers =
      getInputOrElseThrow(InputParser.fromLines(List("4 5"))).mowers
    lawnmowers shouldBe List.empty
  }
}
