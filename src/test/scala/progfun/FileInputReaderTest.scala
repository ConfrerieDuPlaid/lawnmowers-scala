package progfun

import better.files.File
import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import progfun.domain.ports.DonneesIncorrectesException
import progfun.in.InputParser
import progfun.in.file.FileInputReader

import scala.jdk.CollectionConverters.CollectionHasAsScala

@SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
class FileInputReaderTest extends AnyFunSuite with EitherValues with Matchers {
  test("should get file") {
    val expected = InputParser
      .fromLines("""5 5
                   |1 2 N
                   |GAGAGAGAA
                   |3 3 E
                   |AADAADADDA""".stripMargin.lines().toList.asScala.toList)
    FileInputReader.read(File("src/test/resources/progfun/input.test")) should be (expected)
  }

  test("should not get non existing file") {
    val e = FileInputReader.read(File(" non_existing.file ")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[/Users/theo/dev/4AL/Scala/projet/funprog-al/ non_existing.file ] not found.")
  }

  test("and a directory ?") {
    val e = FileInputReader.read(File("src")).left.value
    e shouldBe a[DonneesIncorrectesException]
    e.getMessage should be("[/Users/theo/dev/4AL/Scala/projet/funprog-al/src] not found.")
  }
}
