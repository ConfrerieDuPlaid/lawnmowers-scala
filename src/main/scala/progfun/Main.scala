package progfun

import com.typesafe.config.ConfigFactory
import progfun.config.{CSV, Configuration, FileConfigInput, FileConfigOutput, JSON, YAML}
import progfun.domain.Application
import progfun.out.file.serializers.{CsvResultSerializer, JsonResultSerializer, YamlResultSerializer}
import progfun.out.file.writer.FileWriter
import progfun.in.file.FileMowLawnCommandReader

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
object Main extends App {
  val config = Configuration(ConfigFactory.load());
  config match {
    case Left(e) => throw e
    case Right(configuration) => {
      val reader = configuration.input match {
        case FileConfigInput(file) => FileMowLawnCommandReader.read(file)
      }

      val writer = configuration.output match {
        case FileConfigOutput(file, format) => {
          val serializer = format match {
            case JSON => new JsonResultSerializer()
            case CSV => new CsvResultSerializer()
            case YAML => new YamlResultSerializer()
          }
          new FileWriter(serializer, file);
        }
      }

      val app = new Application(writer)

      val resultEither = reader match {
        case Left(e) => throw e
        case Right(command) => app.mowLawn(command)
      }

      val writed = resultEither match {
        case Left(e) => throw e
        case Right(result) => writer.write(result)
      }

      print(writed.isRight)
      ()
    }
  }

}
