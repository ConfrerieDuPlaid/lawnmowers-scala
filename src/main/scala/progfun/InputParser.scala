package progfun

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object InputParser {
  def fromLines(lines: List[String]): Either[DonneesIncorrectesException,Input] = {
    case class LawnAndMowers(lawn: Lawn, lawnmowers: List[LawnMowerInput])

    val lawnAndMowers = for {
      field <- lawnFromOptionString(lines.headOption)
      lawnMowers <- lawnMowersFrom(lines.drop(1))
    } yield LawnAndMowers(field, lawnMowers)

    lawnAndMowers.flatMap(lawnAndMowers => Input(lawnAndMowers.lawn, lawnAndMowers.lawnmowers))
  }

  private def lawnFromOptionString(input: Option[String]): Either[DonneesIncorrectesException,Lawn] = {
    input match {
      case Some(string) => lawnFrom(string)
      case _ => Left(DonneesIncorrectesException.emptyInput())
    }
  }

  private def lawnFrom(string: String): Either[DonneesIncorrectesException, Lawn] = {
    limitCornerCoordinatesFrom(string) match {
      case Some(coordinate) => Lawn(height = coordinate._1 + 1, width = coordinate._2 + 1)
      case _ => Left(DonneesIncorrectesException.fieldNotParsable(string))
    }
  }

  private def limitCornerCoordinatesFrom(str: String): Option[(Int, Int)] = {
    str match {
      case s"$width $height" => Try((height.toInt, width.toInt)).toOption
      case _ => None
    }
  }

  private def lawnMowersFrom(lines: List[String]): Either[DonneesIncorrectesException, List[LawnMowerInput]] = {
    @tailrec
    def helper(lines: List[String], lawnmowers: List[LawnMowerInput]): Either[DonneesIncorrectesException, List[LawnMowerInput]] = {
      lines match {
        case positionAndOrientation :: instructions :: _ => {
          oneLawnMowerFrom(positionAndOrientation, instructions) match {
            case Right(lawnmower) => helper(lines.drop(2), lawnmowers.appended(lawnmower))
            case Left(e) => Left(e) // cannot use flatmap because of @tailrec
          }
        }
        case Nil => Right(lawnmowers)
        case _ => Left(DonneesIncorrectesException.missingLine())
      }
    }

    helper(lines, List.empty)
  }

  private def oneLawnMowerFrom(startPositionAndOrientationString: String, instructionsString: String): Either[DonneesIncorrectesException, LawnMowerInput] = {
    for {
      positionAndOrientation <- positionAndOrientationFrom(startPositionAndOrientationString)
      instructions <- instructionsFrom(instructionsString)
    } yield LawnMowerInput(position = positionAndOrientation._1, orientation = positionAndOrientation._2, instructions = instructions)
  }

  private def positionAndOrientationFrom(startPositionAndOrientationString: String): Either[DonneesIncorrectesException, (Position, Orientation)] = {
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

  private def instructionsFrom(instructionsString: String): Either[DonneesIncorrectesException, List[Instruction]] = {
    @tailrec
    def helper(chars: List[Char], instructions: List[Instruction]): Either[DonneesIncorrectesException, List[Instruction]] = {
      chars match {
        case char :: _ => oneInstructionFrom(char) match {
          case Right(instruction) => helper(chars.drop(1), instructions.appended(instruction))
          case Left(e) => Left(e) // cannot use flatmap because of @tailrec
        }
        case Nil => Right(instructions)
      }
    }
    helper(instructionsString.toCharArray.toList, List.empty)
  }

  private def oneInstructionFrom(char: Char): Either[DonneesIncorrectesException, Instruction] = {
    char match {
      case 'G' => Right(RotateLeft)
      case 'D' => Right(RotateRight)
      case 'A' => Right(MoveForward)
      case _ => Left(DonneesIncorrectesException.unknownInstruction(char))
    }
  }
}
