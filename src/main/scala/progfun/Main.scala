package progfun

import com.typesafe.config.ConfigFactory
import progfun.config.{CliConfigInput, CliConfigOutput, Configuration, FileConfigInput, FileConfigOutput}
import progfun.domain.Application
import progfun.out.file.serializers.LawnMowedResultSerializerFactory
import progfun.out.file.writer.FileWriter
import progfun.in.file.{CliMowLawnCommandReader, FileMowLawnCommandReader}
import progfun.out.CliWriter

import scala.io.StdIn

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
object Main extends App {
  val config = Configuration(ConfigFactory.load());
  config match {
    case Left(e) => throw e
    case Right(configuration) => {
      val reader = configuration.input match {
        case FileConfigInput(file) => FileMowLawnCommandReader.read(file)
        case CliConfigInput() => CliMowLawnCommandReader.read(StdIn)
      }

      val writer = configuration.output match {
        case FileConfigOutput(file, format) => {
          val serializer = LawnMowedResultSerializerFactory(format)
          new FileWriter(serializer, file);
        }
        case CliConfigOutput(format) => {
          new CliWriter(LawnMowedResultSerializerFactory(format))
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

      print(if (writed.isRight) "" else "")
      ()
    }
  }

}
