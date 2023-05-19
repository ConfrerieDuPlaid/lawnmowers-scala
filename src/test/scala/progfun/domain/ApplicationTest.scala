package progfun.domain

import org.scalatest.funsuite.AnyFunSuite
import progfun.domain.models.{East, Lawn, Lawnmower, MoveForward, MowerWithInstructions, North, Position, RotateLeft, RotateRight}
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.MowLawnCommand
import progfun.domain.ports.out.{LawnMowedResult, MowerResult, Writer}

class ApplicationTest extends AnyFunSuite {
  private val lawn = Lawn(3,3).getOrElse(Lawn.default())
  private val bigLawn = Lawn(5,6).getOrElse(Lawn.default())

  object MockWriter extends Writer {
    override def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult] = Right(lawnMowedResult)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getCommandOrThrow(e: Either[DonneesIncorrectesException, MowLawnCommand]): MowLawnCommand = {
    e match {
      case Right(command) => command
      case Left(exception) => throw exception
    }
  }

  private val mockWriter = MockWriter
  private val application = new Application(mockWriter)

  //  . . .       . . .
  //  . . .   =>  ^ . .
  //  ^ . .       . . .
  test("application should mow lawn with one mower doing one instruction") {
    val command = getCommandOrThrow(MowLawnCommand(
      lawn,
      List(MowerWithInstructions(Lawnmower(Position(0,0), North), List(MoveForward)))
    ))

    assertResult(Right(LawnMowedResult(
      lawn,
      List(MowerResult(
        start = Lawnmower(Position(0,0), North),
        instructions = List(MoveForward),
        end = Lawnmower(Position(0,1), North)
      ))
    ))) {
      application.mowLawn(command)
    }
  }

  //  . . .       ^ . .
  //  . . .   =>  . . .
  //  ^ . .       . . .
  test("application should mow lawn with one mower doing one instruction twice") {
    val command = getCommandOrThrow(MowLawnCommand(
      lawn,
      List(MowerWithInstructions(Lawnmower(Position(0, 0), North), List(MoveForward, MoveForward)))
    ))

    assertResult(Right(LawnMowedResult(
      lawn,
      List(MowerResult(
        start = Lawnmower(Position(0, 0), North),
        instructions = List(MoveForward, MoveForward),
        end = Lawnmower(Position(0, 2), North)
      ))
    ))) {
      application.mowLawn(command)
    }
  }

  //  ^ . .       ^ . .
  //  . . .   =>  . . .
  //  . . .       . . .
  test("mower should not go outside lawn") {
    val command = getCommandOrThrow(MowLawnCommand(
      lawn,
      List(MowerWithInstructions(Lawnmower(Position(0, 2), North), List(MoveForward, MoveForward)))
    ))

    assertResult(Right(LawnMowedResult(
      lawn,
      List(MowerResult(
        start = Lawnmower(Position(0, 2), North),
        instructions = List(MoveForward, MoveForward),
        end = Lawnmower(Position(0, 2), North)
      ))
    ))) {
      application.mowLawn(command)
    }
  }

  //  . . .       . ^ .
  //  . ^ .   =>  ^ . .
  //  ^ . .       . . .
  test("application should mow lawn with two mowers") {
    val forward = List(MoveForward)
    val command = getCommandOrThrow(MowLawnCommand(
      lawn,
      List(
        MowerWithInstructions(Lawnmower(Position(0, 0), North),forward ),
        MowerWithInstructions(Lawnmower(Position(1, 1), North), forward)
      )
    ))

    assertResult(Right(LawnMowedResult(
      lawn,
      List(
        MowerResult(
          start = Lawnmower(Position(0, 0), North),
          instructions = forward,
          end = Lawnmower(Position(0, 1), North)
        ),
        MowerResult(
          start = Lawnmower(Position(1, 1), North),
          instructions = forward,
          end = Lawnmower(Position(1, 2), North)
        )
      )
    ))) {
      application.mowLawn(command)
    }
  }

  test("application should mow lawn with two mowers complex command") {
    val firstMowerInstructions = List(
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

    val secondMowerInstructions = List(
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
    val command = getCommandOrThrow(MowLawnCommand(
      bigLawn,
      List(
        MowerWithInstructions(Lawnmower(Position(1, 2), North), firstMowerInstructions),
        MowerWithInstructions(Lawnmower(Position(3, 3), East), secondMowerInstructions)
      )
    ))

    assertResult(Right(LawnMowedResult(
      bigLawn,
      List(
        MowerResult(
          start = Lawnmower(Position(1, 2), North),
          instructions = firstMowerInstructions,
          end = Lawnmower(Position(1, 3), North)
        ),
        MowerResult(
          start = Lawnmower(Position(3, 3), East),
          instructions = secondMowerInstructions,
          end = Lawnmower(Position(5, 1), East)
        )
      )
    ))) {
      application.mowLawn(command)
    }
  }
}
