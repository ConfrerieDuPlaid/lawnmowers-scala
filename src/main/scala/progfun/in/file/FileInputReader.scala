package progfun.in.file

import better.files.File
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.{Input, InputReader}
import progfun.in.InputParser

case object FileInputReader extends InputReader[File] {
  override def read(file: File): Either[DonneesIncorrectesException, Input] = {
    if(file.isRegularFile) InputParser.fromLines(file.lines.toList)
    else Left(DonneesIncorrectesException.inputFileNotFound(file.pathAsString))
  }
}
