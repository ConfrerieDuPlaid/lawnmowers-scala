package progfun.out.file

import org.scalatest.funsuite.AnyFunSuite
import progfun.domain.models.{East, Lawn, Lawnmower, MoveForward, North, Position, RotateLeft, RotateRight}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}
import progfun.out.file.serializers.YamlResultSerializer

class YamlResultSerializerTest extends AnyFunSuite {

  private val yamlResultSerializer = new YamlResultSerializer()

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getLawnOf(lawn: Either[Exception, Lawn]): Lawn = {
    lawn match {
      case Right(lawn) => lawn
      case Left(e) => throw e
    }
  }

  test("should serialize limit of lawn h=6 x w=4") {
    assertResult("""limite:
                   |  x: 3
                   |  y: 5
                   |tondeuses: []""".stripMargin) {
      yamlResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(height = 6, width = 4)),
          List.empty
        )
      )
    }
  }

  test("should serialize one lawnmower") {
    assertResult(
      """limite:
        |  x: 3
        |  y: 5
        |tondeuses:
        |  - debut:
        |      point:
        |        x: 1
        |        y: 2
        |      direction: N
        |    instructions:
        |      - G
        |      - A
        |      - D
        |    fin:
        |      point:
        |        x: 1
        |        y: 3
        |      direction: N""".stripMargin) {
      yamlResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(height = 6, width = 4)),
          List(
            MowerResult(
              start = Lawnmower(Position(1,2), North),
              instructions = List(RotateLeft, MoveForward, RotateRight),
              end = Lawnmower(Position(1,3), North)
            )
          )
        )
      )
    }
  }

  test("should serialize two lawnmowers") {
    assertResult(
      """limite:
        |  x: 5
        |  y: 5
        |tondeuses:
        |  - debut:
        |      point:
        |        x: 1
        |        y: 2
        |      direction: N
        |    instructions:
        |      - G
        |      - A
        |      - D
        |    fin:
        |      point:
        |        x: 3
        |        y: 3
        |      direction: E
        |  - debut:
        |      point:
        |        x: 3
        |        y: 3
        |      direction: E
        |    instructions:
        |      - A
        |      - D
        |      - G
        |    fin:
        |      point:
        |        x: 5
        |        y: 1
        |      direction: E""".stripMargin) {
      yamlResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(Lawn(height = 6, width = 6)),
          List(
            MowerResult(
              start = Lawnmower(Position(1, 2), North),
              instructions = List(RotateLeft, MoveForward, RotateRight),
              end = Lawnmower(Position(3, 3), East)
            ),
            MowerResult(
              start = Lawnmower(Position(3, 3), East),
              instructions = List(MoveForward, RotateRight, RotateLeft),
              end = Lawnmower(Position(5, 1), East)
            )
          )
        )
      )
    }
  }

}
