package progfun

import org.scalatest.{EitherValues, PrivateMethodTester}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LawnTest extends AnyFunSuite with EitherValues with Matchers with PrivateMethodTester{
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

}
