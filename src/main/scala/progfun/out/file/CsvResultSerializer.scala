package progfun.out.file
import progfun.domain.models.{East, Instruction, MoveForward, North, Orientation, RotateLeft, RotateRight, South, West}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}

import scala.annotation.tailrec

object CsvResultSerializer extends ResultSerializer {
  override def serialize(lawnMowedResult: LawnMowedResult): String = {
    val columns = "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions"
    val lines = columns :: lawnmowers(lawnMowedResult.mowers)
    lines.mkString("\n")
  }

  private def lawnmowers(lawnmowers: List[MowerResult]): List[String] = {
    @tailrec
    def helper(index: Int, mowers: List[MowerResult], lines: List[String]): List[String] = {
      mowers match {
        case mower :: _ =>  helper(index + 1, mowers.drop(1), lines.appended(oneLawnmower(index, mower)))
        case _ => lines
      }
    }

    helper(1, lawnmowers, List.empty)
  }

  private def oneLawnmower(index: Int, lawnmower: MowerResult): String = {
    val start = lawnmower.start
    val end = lawnmower.end
    List(
      index.toString,
      start.position.x.toString,
      start.position.y.toString,
      orientation(start.orientation),
      end.position.x.toString,
      end.position.y.toString,
      orientation(end.orientation),
      instructions(lawnmower.instructions)
    ).mkString(";")
  }

  private def orientation(orientation: Orientation): String = {
    orientation match {
      case North => "N"
      case East => "E"
      case West => "W"
      case South => "S"
      case _ => ""
    }
  }

  private def instructions(instructions: List[Instruction]): String = {
    instructions.map(oneInstruction).mkString
  }

  private def oneInstruction(instruction: Instruction): String = {
    instruction match {
      case RotateLeft => "G"
      case MoveForward => "A"
      case RotateRight => "D"
      case _ => ""
    }
  }

}
