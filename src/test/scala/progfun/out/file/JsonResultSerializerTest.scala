package progfun.out.file

import org.scalatest.funsuite.AnyFunSuite
import progfun.domain.models.{East, Lawn, Lawnmower, MoveForward, North, Position, RotateLeft, RotateRight}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}
import progfun.out.file.serializers.JsonResultSerializer

class JsonResultSerializerTest extends AnyFunSuite {

  private val jsonResultSerializer = new JsonResultSerializer()

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def getLawnOf(height: Int, width: Int): Lawn = {
    Lawn(height, width) match {
      case Right(lawn) => lawn
      case Left(e) => throw e
    }
  }

  test("should serialize field limit of lawn h=6 x w=6") {
    assertResult("""{
                   |  "limite": {
                   |    "x": 5,
                   |    "y": 5
                   |  },
                   |  "tondeuses": []
                   |}""".stripMargin) {
      jsonResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(height = 6, width = 6),
          List.empty
        )
      )
    }
  }

  test("should serialize field limit of lawn h=2 x w=8") {
    assertResult(
      """{
        |  "limite": {
        |    "x": 7,
        |    "y": 1
        |  },
        |  "tondeuses": []
        |}""".stripMargin) {
      jsonResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(height = 2, width = 8),
          List.empty
        )
      )
    }
  }

  test("should serialize one lawnmower") {
    assertResult(
      """{
        |  "limite": {
        |    "x": 5,
        |    "y": 5
        |  },
        |  "tondeuses": [
        |    {
        |      "debut": {
        |        "point": {
        |          "x": 1,
        |          "y": 2
        |        },
        |        "direction": "N"
        |      },
        |      "instructions": ["G","A","D"],
        |      "fin": {
        |        "point": {
        |          "x": 1,
        |          "y": 3
        |        },
        |        "direction": "N"
        |      }
        |    }
        |  ]
        |}""".stripMargin) {
      jsonResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(height = 6, width = 6),
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
      """{
        |  "limite": {
        |    "x": 5,
        |    "y": 5
        |  },
        |  "tondeuses": [
        |    {
        |      "debut": {
        |        "point": {
        |          "x": 1,
        |          "y": 2
        |        },
        |        "direction": "N"
        |      },
        |      "instructions": ["G","A","D"],
        |      "fin": {
        |        "point": {
        |          "x": 1,
        |          "y": 3
        |        },
        |        "direction": "N"
        |      }
        |    },
        |    {
        |      "debut": {
        |        "point": {
        |          "x": 3,
        |          "y": 3
        |        },
        |        "direction": "E"
        |      },
        |      "instructions": ["A","G","D"],
        |      "fin": {
        |        "point": {
        |          "x": 5,
        |          "y": 1
        |        },
        |        "direction": "E"
        |      }
        |    }
        |  ]
        |}""".stripMargin) {
      jsonResultSerializer.serialize(
        LawnMowedResult(
          getLawnOf(height = 6, width = 6),
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
