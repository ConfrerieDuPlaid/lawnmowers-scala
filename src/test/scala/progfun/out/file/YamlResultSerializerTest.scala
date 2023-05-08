package progfun.out.file

import org.scalatest.funsuite.AnyFunSuite

class YamlResultSerializerTest extends AnyFunSuite {
/*
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
    YamlResultSerializer.serialize(
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
    YamlResultSerializer.serialize(
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
*/

}
