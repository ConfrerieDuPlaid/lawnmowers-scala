package progfun.out

import progfun.domain.ports.DonneesIncorrectesException
import progfun.domain.ports.out.{LawnMowedResult, Writer}
import progfun.out.file.serializers.Serializer

class CliWriter(serializer: Serializer[LawnMowedResult]) extends Writer{
  override def write(lawnMowedResult: LawnMowedResult): Either[DonneesIncorrectesException, LawnMowedResult] = {
    print(serializer.serialize(lawnMowedResult))
    Right(lawnMowedResult)
  }
}
