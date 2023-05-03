package progfun

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object InputParser {
  def fromLines(lines: List[String]): Either[DonneesIncorrectesException,SetUp] = {
    for {
      field <- fieldFromOptionString(lines.headOption)
      lawnMowers <- lawnMowersFrom(lines.drop(1))
    } yield SetUp(field, lawnMowers)
  }

  private def fieldFromOptionString(input: Option[String]): Either[DonneesIncorrectesException,Field] = {
    input match {
      case Some(string) => fieldFrom(string)
      case _ => Left(DonneesIncorrectesException.emptyInput())
    }
  }

  private def fieldFrom(string: String): Either[DonneesIncorrectesException, Field] = {
    limitCornerPositionFrom(string) match {
      case Some(position) => Field(height = position._1 + 1, width = position._2 + 1)
      case _ => Left(DonneesIncorrectesException.fieldNotParsable(string))
    }
  }

  private def limitCornerPositionFrom(str: String): Option[(Int, Int)] = {
    str match {
      case s"$width $height" => Try((height.toInt, width.toInt)).toOption
      case _ => None
    }
  }

  // TODO Refactor
  private def lawnMowersFrom(lines: List[String]): Either[DonneesIncorrectesException, List[LawnMowerInput]] = {
    @tailrec
    def helper(lines: List[String], lawnmowers: List[LawnMowerInput]): Either[DonneesIncorrectesException, List[LawnMowerInput]] = {
      if(lines.isEmpty) Right(lawnmowers)
      else {
        twoFirstLinesOf(lines) match {
          case Some(twoLines) => {
            oneLawnMowerFrom(startPositionAndOrientationString = twoLines._1) match {
              case Right(lawnmower) => helper(lines.drop(2), lawnmowers.appended(lawnmower))
              case Left(e) => Left(e)
            }
          }
          case _ => Left(DonneesIncorrectesException.missingLine())
        }
      }
    }

    helper(lines, List.empty)
  }

  private def twoFirstLinesOf(lines: List[String]): Option[(String, String)] = {
    lines match {
      case firstLine :: secondLine :: _ => Some((firstLine, secondLine))
      case _ => None
    }
  }

  private def oneLawnMowerFrom(startPositionAndOrientationString: String): Either[DonneesIncorrectesException, LawnMowerInput] = {
    for {
      positionAndOrientation <- startPositionAndOrientation(startPositionAndOrientationString)
      // TODO Instructions instructionsString
    } yield LawnMowerInput(Start(position = positionAndOrientation._1, orientation = positionAndOrientation._2))
  }

  private def startPositionAndOrientation(startPositionAndOrientationString: String): Either[DonneesIncorrectesException, (Position, Orientation)] = {
    startPositionAndOrientationString match {
      case s"$x $y $orientation" => {
        for {
          position <- positionFrom(x = x, y = y)
          orientation <- orientationFrom(orientation)
        } yield (position, orientation)
      }
      case _ => Left(DonneesIncorrectesException.startPositionAndOrientationNotParsable(startPositionAndOrientationString))
    }
  }

  private def positionFrom(x: String, y: String): Either[DonneesIncorrectesException, Position] = {
    Try(Position(x = x.toInt, y = y.toInt)) match {
      case Success(position) => Right(position)
      case Failure(_) => Left(DonneesIncorrectesException.positionNotParsable(x = x, y = y))
    }
  }

  private def orientationFrom(orientation: String): Either[DonneesIncorrectesException, Orientation] = {
    orientation match {
      case "N" => Right(North)
      case "E" => Right(East)
      case "W" => Right(West)
      case "S" => Right(South)
      case _ => Left(DonneesIncorrectesException.orientationNotParsable(orientation))
    }
  }
}
