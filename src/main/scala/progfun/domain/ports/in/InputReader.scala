package progfun.domain.ports.in

import progfun.domain.ports.DonneesIncorrectesException

trait InputReader[From] {
  def read(from: From): Either[DonneesIncorrectesException, Input]
}
