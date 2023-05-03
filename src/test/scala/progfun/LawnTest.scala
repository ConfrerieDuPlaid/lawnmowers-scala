package progfun

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LawnTest extends AnyFunSuite with EitherValues with Matchers{
  test("default lawn should be 1x1") {
    val default = Lawn.default()
    assert(default.width === 1)
    assert(default.height === 1)
  }

  test("should create lawn") {
    val f = Lawn(5,7).getOrElse(Lawn.default())
    assert(f.height === 5)
    assert(f.width === 7)
  }

  test("should get left when limits are not strictly positive") {
    val e = Lawn(5,0).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("Lawn's limits must be strictly positive. Given = [height=5, width=0].")
  }

  test("should ensure a position is inside lawn") {
    assert(Lawn(2,2).getOrElse(Lawn.default()).containsPosition(Position(1,0)))
  }

  test("should ensure a position is not inside lawn") {
    val lawn = Lawn(2,2).getOrElse(Lawn.default())
    assert(!lawn.containsPosition(Position(-1, 0)))
    assert(!lawn.containsPosition(Position(0, -1)))
    assert(!lawn.containsPosition(Position(0, 2)))
    assert(!lawn.containsPosition(Position(2, 0)))
  }

}
