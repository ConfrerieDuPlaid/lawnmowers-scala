package progfun.out.file

import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.out.{LawnMowedResult, Writer}

class FileWriter(serializer: ResultSerializer) extends Writer{
  override def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult] = {
    val serialized = serializer.serialize(lawnMowedResult)
    if(serialized.isEmpty) Left(DonneesIncorrectesException.writingError())
    else Right(lawnMowedResult)
  }
}
