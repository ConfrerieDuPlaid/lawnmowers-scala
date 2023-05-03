package progfun

final case class Lawn private(height: Int, width: Int) {
  def containsPosition(position: Position): Boolean = {
    position.x >= 0 && position.y >= 0 && position.x < width && position.y < height
  }

}

object Lawn {
  def apply(height: Int, width: Int): Either[DonneesIncorrectesException, Lawn] = {
    if(height > 0 && width > 0) Right(new Lawn(height, width))
    else Left(DonneesIncorrectesException.negativeLimits(height, width))
  }

  def default(): Lawn = {
    new Lawn(height = 1, width = 1)
  }
}