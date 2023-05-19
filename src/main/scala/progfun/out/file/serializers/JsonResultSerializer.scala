package progfun.out.file.serializers
import progfun.domain.models.{East, Instruction, MoveForward, North, Orientation, Position, RotateLeft, RotateRight, South, West}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}

class JsonResultSerializer extends JsonSerializer[LawnMowedResult] {
  override def serialize(lawnMowedResult: LawnMowedResult): String = {
    val lawn = lawnMowedResult.lawn
    val limit = Position(lawn.width - 1, lawn.height - 1)
    s"""{
       |  "limite": ${position(limit, 2)},
       |  "tondeuses": ${lawnmowers(lawnMowedResult.mowers)}
       |}""".stripMargin
  }

  private def lawnmowers(mowers: List[MowerResult]): String = {
    if(mowers.isEmpty) "[]"
    else
      s"""[
         |${mowers.map(oneMowerResult).mkString(",\n")}
         |  ]""".stripMargin
  }

  private def oneMowerResult(result: MowerResult): String = {
    val start = result.start
    val end = result.end
    s"""    {
      |      "debut": {
      |        "point": ${position(start.position, 8)},
      |        "direction": "${orientation(result.start.orientation)}"
      |      },
      |      "instructions": ${instructions(result.instructions)},
      |      "fin": {
      |        "point": ${position(end.position, 8)},
      |        "direction": "${orientation(end.orientation)}"
      |      }
      |    }""".stripMargin
  }

  private def position(position: Position, indent: Int): String = {
    s"""{
       |${indentation(indent+2)}"x": ${position.x.toString},
       |${indentation(indent+2)}"y": ${position.y.toString}
       |${indentation(indent)}}""".stripMargin
  }

  private def indentation(space: Int): String = {
    List.fill(space)(" ").mkString
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
    s"[${
      instructions
        .map(oneInstruction)
        .map(i => "\"" + i + "\"")
        .mkString(",")
    }]"
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