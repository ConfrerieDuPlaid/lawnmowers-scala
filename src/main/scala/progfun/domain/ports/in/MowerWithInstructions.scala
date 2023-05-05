package progfun.domain.ports.in

import progfun.domain.models.{Instruction, Lawnmower}
final case class MowerWithInstructions(mower: Lawnmower, instructions: List[Instruction])
