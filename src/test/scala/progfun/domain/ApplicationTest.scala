package progfun.domain

import org.scalatest.funsuite.AnyFunSuite
import progfun.domain.models.{Lawn, Lawnmower, MoveForward, North, Position}
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.{MowLawnCommand, MowerWithInstructions}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}

class ApplicationTest extends AnyFunSuite {
  private val lawn = Lawn(3,3).getOrElse(Lawn.default())

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getCommandOrThrow(e: Either[DonneesIncorrectesException, MowLawnCommand]): MowLawnCommand = {
    e match {
      case Right(command) => command
      case Left(exception) => throw exception
    }
  }

  private val application = new Application()

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
    val forward = List(MoveForward);
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
}
