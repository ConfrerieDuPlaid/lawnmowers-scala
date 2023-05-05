package progfun.domain.models

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class OrientationTest extends AnyFunSuite with Matchers {
  val position = Position(2,2)

  test("Move to North should increase Y coordinates") {
    North.of(position) shouldBe Position(2,3)
  }

  test("Move to South should decrease Y coordinates") {
    South.of(position) shouldBe Position(2, 1)
  }

  test("Move to East should increase X coordinates") {
    East.of(position) shouldBe Position(3, 2)
  }

  test("Move to West should decrease X coordinates") {
    West.of(position) shouldBe Position(1, 2)
  }
}
