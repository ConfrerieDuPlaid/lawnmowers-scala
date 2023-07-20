package progfun.config

import better.files.File
import com.typesafe.config.Config
import progfun.domain.ports.DonneesIncorrectesException

import scala.util.{Failure, Success, Try}

case class Configuration (input: ConfigInput, output: ConfigOutput)

object Configuration {

  private def getConfigValue(config: Config, key: String): Either[DonneesIncorrectesException, String] = {
    Try(config.getString(key)) match {
      case Failure(_) => Left(DonneesIncorrectesException.missingConfiguration(key))
      case Success(value) => Right(value)
    }
  }

  def cliOutput(config: Config): Either[DonneesIncorrectesException, ConfigOutput] = {
    outputFormat(config).flatMap(format => Right(CliConfigOutput(format)))
  }

  def apply(config: Config): Either[DonneesIncorrectesException, Configuration] = {

    val input: Either[DonneesIncorrectesException, ConfigInput] = getConfigValue(config, "app.input_mode").flatMap(inputMode => {
      inputMode.toUpperCase.trim match {
        case "FILE" => fileInput(config)
        case "CLI" => Right(CliConfigInput())
        case _ => Left(DonneesIncorrectesException.unkownInputMode(inputMode))
      }
    })


    val output: Either[DonneesIncorrectesException, ConfigOutput] = getConfigValue(config, "app.output_mode").flatMap(outputMode => {
      outputMode.toUpperCase.trim match {
        case "FILE" => fileOutput(config)
        case "CLI" => cliOutput(config)
        case _ => Left(DonneesIncorrectesException.unkownOutputMode(outputMode))
      }
    })

    for {
      in <- input
      out <- output
    } yield new Configuration(in, out)
  }

  private def fileInput(config: Config): Either[DonneesIncorrectesException, FileConfigInput] = {
    getConfigValue(config, "file_input.path")
      .flatMap(path => {
      val file = File(path)

      if (file.isRegularFile) Right(FileConfigInput(file))
      else Left(DonneesIncorrectesException.inputFileNotFound(path))
    })

  }

  private def fileOutput(config: Config): Either[DonneesIncorrectesException, FileConfigOutput] = {
    def path = config.getString("file_output.path")
    val file = File.apply(path)

    if(!file.isRegularFile) Left(DonneesIncorrectesException.outputFileNotFound(path))
    else outputFormat(config).flatMap(format => Right(FileConfigOutput(file, format)))
  }

  private def outputFormat(config: Config): Either[DonneesIncorrectesException, OutputFormat] = {
    val format = config.getString("app.output_format")
    format.toUpperCase.trim match {
      case "JSON" => Right(JSON)
      case "CSV" => Right(CSV)
      case "YAML" | "YML" => Right(YAML)
      case _ => Left(DonneesIncorrectesException.unkownOutputFileFormat(format))
    }
  }
}
