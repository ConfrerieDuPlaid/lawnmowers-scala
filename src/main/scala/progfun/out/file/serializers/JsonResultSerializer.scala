package progfun.out.file.serializers
import progfun.domain.models.{East, Instruction, MoveForward, North, Orientation, RotateLeft, RotateRight, South, West}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult}
import progfun.out.file.serializers.Serializer

case object JsonResultSerializer extends Serializer[LawnMowedResult] {
  override def serialize(lawnMowedResult: LawnMowedResult): String = {
    val lawn = lawnMowedResult.lawn
    val (x,y) = (lawn.width - 1, lawn.height - 1)
    s"""{
       |  "limite": {
       |    "x": ${x.toString},
       |    "y": ${y.toString}
       |  },
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
      |        "point": {
      |          "x": ${start.position.x.toString},
      |          "y": ${start.position.y.toString}
      |        },
      |        "direction": "${orientation(result.start.orientation)}"
      |      },
      |      "instructions": ${instructions(result.instructions)},
      |      "fin": {
      |        "point": {
      |          "x": ${end.position.x.toString},
      |          "y": ${end.position.y.toString}
      |        },
      |        "direction": "${orientation(end.orientation)}"
      |      }
      |    }""".stripMargin
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