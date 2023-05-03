package progfun

final case class Input private (lawn: Lawn, lawnMowers: List[LawnMowerInput])

object Input {
  def apply(lawn: Lawn, mowers: List[LawnMowerInput]) : Either[DonneesIncorrectesException, Input] = {
    val mowersOutsideLawn = mowers.filter(mower => !lawn.containsPosition(mower.position))
    mowersOutsideLawn match {
      case Nil => Right(new Input(lawn, mowers))
      case mower :: _ => Left(DonneesIncorrectesException.lawnmowerStartOutsideLawn(mower))
    }
  }
}