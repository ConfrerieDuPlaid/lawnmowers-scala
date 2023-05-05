package progfun.domain.ports.in

import progfun.domain.models.Lawn
import progfun.domain.ports.DonneesIncorrectesException

final case class MowLawnCommand private(lawn: Lawn, mowers: List[MowerWithInstructions])

object MowLawnCommand {
  def apply(
      lawn: Lawn,
      mowers: List[MowerWithInstructions]
  ): Either[DonneesIncorrectesException, MowLawnCommand] = {
    val mowersOutsideLawn = mowers
      .map(m => m.lawnmower)
      .filter(mower => !lawn.contains(mower))
    mowersOutsideLawn match {
      case Nil => Right(new MowLawnCommand(lawn, mowers))
      case mower :: _ =>
        Left(DonneesIncorrectesException.lawnmowerStartOutsideLawn(mower))
    }
  }
}
