package progfun.domain.ports.out

import progfun.domain.models.{Instruction, Lawnmower}

final case class MowerResult(start: Lawnmower, instructions: List[Instruction], end: Lawnmower)
