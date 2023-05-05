package progfun.domain.ports.in

import progfun.domain.models.Lawn
import progfun.domain.ports.DonneesIncorrectesException

final case class Input private (lawn: Lawn, lawnMowers: List[MowerWithInstructions])

object Input {
  def apply(lawn: Lawn, mowers: List[MowerWithInstructions]) : Either[DonneesIncorrectesException, Input] = {
    val mowersOutsideLawn = mowers.map(m => m.mower).filter(mower => !lawn.containsPosition(mower.position))
    mowersOutsideLawn match {
      case Nil => Right(new Input(lawn, mowers))
      case mower :: _ => Left(DonneesIncorrectesException.lawnmowerStartOutsideLawn(mower))
    }
  }
}