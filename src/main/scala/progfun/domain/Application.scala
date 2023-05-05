package progfun.domain

import progfun.domain.models.{Instruction, Lawn, Lawnmower}
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.in.{MowLawnCommand, MowLawnService, MowerWithInstructions}
import progfun.domain.ports.out.{LawnMowedResult, MowerResult, Writer}

import scala.annotation.tailrec

final class Application(writer: Writer) extends MowLawnService{

  override def mowLawn(command: MowLawnCommand): Either[DonneesIncorrectesException, LawnMowedResult] = {
    val lawn = command.lawn
    val results = command.mowers.map(lawnmower => mowAndGetResult(lawn, lawnmower))

    writer.write(LawnMowedResult(command.lawn, results))
  }


  @tailrec
  private def mow(lawn: Lawn, lawnmower: Lawnmower, instructions: List[Instruction]): Lawnmower = {
    instructions match {
      case instruction :: _ => {
        val mowerAfterInstruction = instruction.execute(lawnmower)
        if(lawn.contains(mowerAfterInstruction)) mow(lawn, mowerAfterInstruction, instructions.drop(1))
        else lawnmower
      }
      case Nil => lawnmower
    }
  }

  private def mowAndGetResult(lawn: Lawn, mowerWithInstructions: MowerWithInstructions): MowerResult = {
    val instructions = mowerWithInstructions.instructions
    val mower = mowerWithInstructions.lawnmower
    MowerResult(
      mower,
      instructions,
      mow(lawn, mower, instructions)
    )
  }

}
