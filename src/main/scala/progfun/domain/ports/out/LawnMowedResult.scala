package progfun.domain.ports.out

import progfun.domain.models.Lawn

final case class LawnMowedResult(lawn: Lawn, mowers: List[MowerResult])
