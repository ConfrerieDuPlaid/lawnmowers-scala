package progfun.out.file

import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.out.{LawnMowedResult, Writer}

class FileWriter extends Writer{
  override def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult] = {
    Right(lawnMowedResult)
  }
}
