package progfun

import com.typesafe.config.ConfigFactory
import progfun.config.{CliConfigInput, CliConfigOutput, Configuration, FileConfigInput, FileConfigOutput}
import progfun.domain.Application
import progfun.out.file.serializers.LawnMowedResultSerializerFactory
import progfun.out.file.writer.FileWriter
import progfun.in.file.{CliMowLawnCommandReader, FileMowLawnCommandReader}
import progfun.out.CliWriter

import scala.io.StdIn

object Main extends App {
  val out = Configuration(ConfigFactory.load())
    .flatMap(configuration =>
    {
      val command = configuration.input match {
      case FileConfigInput(file) => FileMowLawnCommandReader.read(file)
      case CliConfigInput() => CliMowLawnCommandReader.read(StdIn)
    }

    val writer = configuration.output match {
      case FileConfigOutput(file, format) => new FileWriter(LawnMowedResultSerializerFactory(format), file)
      case CliConfigOutput(format) => new CliWriter(LawnMowedResultSerializerFactory(format))
    }

      val app = new Application(writer)
      for {
        c <- command
        result <- app.mowLawn(c)
        writed <- writer.write(result)
      } yield writed

  });

  print(out match {
    case Left(error) => error.getMessage
    case Right(_) => ""
  })
}
