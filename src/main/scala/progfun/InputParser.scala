package progfun

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object InputParser {
  def fromLines(lines: List[String]): Either[DonneesIncorrectesException,Input] = {
    for {
      field <- fieldFromOptionString(lines.headOption)
      lawnMowers <- lawnMowersFrom(lines.drop(1))
    } yield Input(field, lawnMowers)
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
      lines match {
        case positionAndOrientation :: instructions :: _ => {
          oneLawnMowerFrom(positionAndOrientation, instructions) match {
            case Right(lawnmower) => helper(lines.drop(2), lawnmowers.appended(lawnmower))
            case Left(e) => Left(e)
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
          case Left(e) => Left(e)
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
      case _ => Left(DonneesIncorrectesException.unkownInstruction(char))
    }
  }
}
