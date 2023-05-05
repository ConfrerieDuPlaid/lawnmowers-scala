package progfun.domain.ports.in

import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.out.LawnMowedResult

trait MowLawnService {
  def mowLawn(command: MowLawnCommand): Either[DonneesIncorrectesException, LawnMowedResult]
}
