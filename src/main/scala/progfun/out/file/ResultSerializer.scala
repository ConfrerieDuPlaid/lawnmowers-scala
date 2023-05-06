package progfun.out.file

import progfun.domain.ports.out.LawnMowedResult

trait ResultSerializer {
  def serialize(lawnMowedResult: LawnMowedResult): String
}
