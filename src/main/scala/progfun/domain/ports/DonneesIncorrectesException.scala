package progfun.domain.ports

import progfun.domain.models.Lawnmower

final case class DonneesIncorrectesException private (
    private val message: String
) extends Exception(message) {}

object DonneesIncorrectesException {
  def missingConfiguration(configName: String) = new DonneesIncorrectesException(s"Configuration [$configName] is missing.")

  def unkownOutputFileFormat(value: String) = new DonneesIncorrectesException(s"Format [$value] is not a supported output format.")


  def unkownOutputMode(value: String) = new DonneesIncorrectesException(s"Unknown input mode [$value].")

  def unkownInputMode(value: String) = new DonneesIncorrectesException(s"Unknown input mode [$value].")

  def writingError(): DonneesIncorrectesException = new DonneesIncorrectesException("writing error")


  def inputFileNotFound(path: String) = new DonneesIncorrectesException(s"Input [$path] not found.")

  def outputFileNotFound(path: String) = new DonneesIncorrectesException(s"Output [$path] not found.")

  def lawnmowerStartOutsideLawn(mower: Lawnmower) = {
    val p = mower.position
    new DonneesIncorrectesException(
      s"Lawnmower with start position (${p.x.toString},${p.y.toString}) is outside lawn."
    )
  }

  def unknownInstruction(instruction: Char) =
    new DonneesIncorrectesException(
      s"[${instruction.toString}] is not an instruction. Accepted values are (G,D,A)."
    )

  def orientationNotParsable(orientation: String) =
    new DonneesIncorrectesException(
      s"[$orientation] is not an orientation. Accepted values are (N,E,W,S)."
    )

  def positionNotParsable(x: String, y: String) =
    new DonneesIncorrectesException(s"[$x,$y] not parsable to a position.")

  def startPositionAndOrientationNotParsable(input: String) =
    new DonneesIncorrectesException(
      s"$input is not parsable to position and orientation. Expected format like 'x y z'."
    )
  def missingLine() = new DonneesIncorrectesException(
    "Missing line. Each lawnmower should be defined by a first line for the start position and orientation, and a second line for the instructions."
  )
  def negativeLimits(height: Int, width: Int) =
    new DonneesIncorrectesException(
      s"Lawn's limits must be strictly positive. Given = [height=${height.toString}, width=${width.toString}]."
    )
  def fieldNotParsable(input: String) =
    new DonneesIncorrectesException(s"$input is not parsable to field limit.")
  def emptyInput() = new DonneesIncorrectesException("Empty Input.")

}
