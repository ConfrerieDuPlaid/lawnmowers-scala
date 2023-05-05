package progfun.domain.models

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class InstructionTest extends AnyFunSuite with Matchers {

  private val north = Lawnmower(Position(0,0), North)
  private val east = Lawnmower(Position(0,0), East)
  private val west = Lawnmower(Position(0,0), West)
  private val south = Lawnmower(Position(0,0), South)

  // RotateLeft
  test("Rotate to left from north should be west") {
    RotateLeft.execute(north) shouldBe west
  }

  test("Rotate to left from west should be south") {
    RotateLeft.execute(west) shouldBe south
  }

  test("Rotate to left from south should be east") {
    RotateLeft.execute(south) shouldBe east
  }

  test("Rotate to left from east should be north") {
    RotateLeft.execute(east) shouldBe north
  }


  // RotateRight
  test("Rotate to right from north should be east") {
    RotateRight.execute(north) shouldBe east
  }

  test("Rotate to right from east should be south") {
    RotateRight.execute(east) shouldBe south
  }

  test("Rotate to right from south should be west") {
    RotateRight.execute(south) shouldBe west
  }

  test("Rotate to right from west should be north") {
    RotateRight.execute(west) shouldBe north
  }

  // MoveForward
  test("Move should change position") {
    MoveForward.execute(north).position should not be north.position
  }
}
