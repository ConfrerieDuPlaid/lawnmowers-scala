package progfun.domain.ports.out

import progfun.domain.ports.DonneesIncorrectesException

trait Writer {
  def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult]
}
