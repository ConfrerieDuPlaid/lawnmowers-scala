package progfun.in.file

import better.files.File
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.{MowLawnCommand, MowLawnCommandReader}
import progfun.in.InputParser

case object FileMowLawnCommandReader extends MowLawnCommandReader[File] {
  override def read(file: File): Either[DonneesIncorrectesException, MowLawnCommand] = {
    if (file.isRegularFile) InputParser.fromLines(file.lines.toList)
    else Left(DonneesIncorrectesException.inputFileNotFound(file.pathAsString))
  }
}
