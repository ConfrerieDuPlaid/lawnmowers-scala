package progfun

final case class DonneesIncorrectesException (private val message: String) extends Exception(message) {}

object DonneesIncorrectesException {
  def negativeLimits(height: Int, width: Int): DonneesIncorrectesException = new DonneesIncorrectesException(s"Fields limits must be strictly positive. Given = [height=${height.toString}, width=${width.toString}].")

  def fieldNotParsable(input: String): DonneesIncorrectesException = new DonneesIncorrectesException(s"$input is not parsable to field limit.")

  def emptyInput(): DonneesIncorrectesException = new DonneesIncorrectesException("Empty Input.")

}
