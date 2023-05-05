package progfun.domain.port.in

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import progfun.domain.models.{Lawn, Lawnmower, North, Position}
import progfun.domain.ports.in.{MowLawnCommand, MowerWithInstructions}
import progfun.domain.ports.{DonneesIncorrectesException, in}

class MowLawnCommandTest extends AnyFunSuite with EitherValues with Matchers {

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getInputOrElseThrow(either: Either[Exception, MowLawnCommand]): MowLawnCommand = {
    either match {
      case Right(input)    => input
      case Left(exception) => throw exception
    }
  }

  test("should create an input") {
    val inputEither = MowLawnCommand(
      Lawn.default(),
      List(MowerWithInstructions(Lawnmower(Position(0, 0), North), List.empty))
    )
    val input = getInputOrElseThrow(inputEither)
    assert(input.lawn === Lawn.default())
    assert(
      input.mowers === List(
        in.MowerWithInstructions(Lawnmower(Position(0, 0), North), List.empty)
      )
    )
  }

  test("should get left when a lawnmower is outside lawn") {
    val e = MowLawnCommand(
      Lawn.default(),
      List(
        in.MowerWithInstructions(Lawnmower(Position(0, 2), North), List.empty)
      )
    ).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be(
      "Lawnmower with start position (0,2) is outside lawn."
    )
  }
}
