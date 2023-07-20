package progfun.out.file.writer

import better.files.File
import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.out.{LawnMowedResult, Writer}
import progfun.out.file.serializers.Serializer

class FileWriter(serializer: Serializer[LawnMowedResult], file: File) extends Writer{
  override def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult] = {
    val serialized = serializer.serialize(lawnMowedResult)
    if(serialized.isEmpty) Left(DonneesIncorrectesException.writingError())
    else {
      file.write(serialized)
      Right(lawnMowedResult)
    }
  }
}
