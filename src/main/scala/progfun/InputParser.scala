package progfun

import scala.util.Try

object InputParser {

  def fromLines(lines: List[String]): Either[Exception,SetUp] = {
    for {
      field <- fieldFromOptionString(lines.headOption)
    } yield SetUp(field)
  }

  private def fieldFromOptionString(input: Option[String]): Either[Exception,Field] = {
    input match {
      case Some(string) => fieldFromString(string)
      case None => Left(DonneesIncorrectesException.emptyInput())
    }
  }
  private def fieldFromString(str: String): Either[Exception, Field] = {
    val fieldOption = str match {
      case s"$width $height" => Try((height.toInt, width.toInt)).toOption
      case _ => None
    }
    fieldOption match {
      case Some(tuple) => Field(tuple._1, tuple._2)
      case None => Left(DonneesIncorrectesException.fieldNotParsable(str))
    }
  }
}
