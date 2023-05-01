package progfun

final case class Field private(height: Int, width: Int) {}

object Field {
  def apply(height: Int, width: Int): Either[DonneesIncorrectesException, Field] = {
    if(height > 0 && width > 0) Right(new Field(height, width))
    else Left(DonneesIncorrectesException.negativeLimits(height, width))
  }

  def default(): Field = {
    new Field(height = 1, width = 1)
  }
}