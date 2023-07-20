package progfun.in.file

import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.{MowLawnCommand, MowLawnCommandReader}
import progfun.in.InputParser

import scala.annotation.tailrec
import scala.io.StdIn

case object CliMowLawnCommandReader extends MowLawnCommandReader[StdIn.type] {
  override def read(from: StdIn.type): Either[DonneesIncorrectesException, MowLawnCommand] = {
    InputParser.fromLines(readInputTextFromStdin(from))
  }

  def readInputTextFromStdin(from: StdIn.type): List[String] = {
    val lines = readLinesRecursively(from.readLine(), "")
    lines.split("\n").toList
  }

  @tailrec
  private def readLinesRecursively(line: String, acc: String): String = {
    if (line == null || line.isEmpty)
      acc
    else {
      val newAcc = if (acc.isEmpty) line else s"$acc\n$line"
      readLinesRecursively(StdIn.readLine(), newAcc)
    }
  }
}
