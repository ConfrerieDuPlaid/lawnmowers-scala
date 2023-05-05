package progfun.domain.ports.in

import progfun.domain.ports.DonneesIncorrectesException

trait MowLawnCommandReader[From] {
  def read(from: From): Either[DonneesIncorrectesException, MowLawnCommand]
}
