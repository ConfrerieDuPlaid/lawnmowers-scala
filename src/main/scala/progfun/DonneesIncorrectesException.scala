package progfun

final case class DonneesIncorrectesException (private val message: String) extends Exception(message) {}

object DonneesIncorrectesException {
  def orientationNotParsable(orientation: String) = new DonneesIncorrectesException(s"[$orientation] not parsable to Orientation. Accepted values are (N,E,W,S)")

  def positionNotParsable(x: String, y: String) = new DonneesIncorrectesException(s"[$x,$y] not parsable to Position.")

  def startPositionAndOrientationNotParsable(input: String) = new DonneesIncorrectesException(s"$input is not parsable to position and orientation.")

  def missingLine() = new DonneesIncorrectesException(
    "Missing line. Lawnmowers should be defined by a first line for the start position and orientation, and a second line for the instructions"
  )
  def negativeLimits(height: Int, width: Int) = new DonneesIncorrectesException(s"Fields limits must be strictly positive. Given = [height=${height.toString}, width=${width.toString}].")
  def fieldNotParsable(input: String) = new DonneesIncorrectesException(s"$input is not parsable to field limit.")
  def emptyInput() = new DonneesIncorrectesException("Empty Input.")

}
