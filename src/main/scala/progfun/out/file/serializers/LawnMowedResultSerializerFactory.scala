package progfun.out.file.serializers

import progfun.config.{CSV, JSON, OutputFormat, YAML}
import progfun.domain.ports.out.LawnMowedResult

object LawnMowedResultSerializerFactory {
  def apply(format: OutputFormat): Serializer[LawnMowedResult] = {
    format match {
      case JSON => new JsonResultSerializer()
      case CSV => new CsvResultSerializer()
      case YAML => new YamlResultSerializer()
    }
  }
}
