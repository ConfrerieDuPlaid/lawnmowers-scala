package progfun.out.file

import org.scalatest.funsuite.AnyFunSuite
import progfun.domain.models.{East, Lawn, Lawnmower, MoveForward, North, Position, RotateLeft, RotateRight}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}
import progfun.out.file.serializers.CsvResultSerializer

class CsvResultSerializerTest extends AnyFunSuite{
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getLawnOf(lawn: Either[Exception, Lawn]): Lawn = {
    lawn match {
      case Right(lawn) => lawn
      case Left(e) => throw e
    }
  }

  test("should serialize columns of csv") {
    assertResult("numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions") {
      CsvResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(6,6)),
          List.empty
        )
      )
    }
  }

  test("should serialize one lawnmower") {
    assertResult(
      """numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions
        |1;1;2;N;1;3;N;GAD""".stripMargin) {
      CsvResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(6, 6)),
          List(MowerResult(
            start = Lawnmower(Position(1,2), North),
            instructions = List(RotateLeft, MoveForward, RotateRight),
            end = Lawnmower(Position(1,3), North)
          ))
        )
      )
    }
  }

  test("should serialize two lawnmowers") {
    assertResult(
      """numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions
        |1;1;2;N;1;3;N;GAD
        |2;3;3;E;5;1;E;AGD""".stripMargin) {
      CsvResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(6, 6)),
          List(
            MowerResult(
              start = Lawnmower(Position(1, 2), North),
              instructions = List(RotateLeft, MoveForward, RotateRight),
              end = Lawnmower(Position(1, 3), North)
            ),
            MowerResult(
              start = Lawnmower(Position(3, 3), East),
              instructions = List(MoveForward, RotateLeft, RotateRight),
              end = Lawnmower(Position(5, 1), East)
            )
          )
        )
      )
    }
  }
}
