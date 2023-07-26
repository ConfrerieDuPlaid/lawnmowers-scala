package progfun.configuration

import better.files.File
import com.typesafe.config.ConfigFactory
import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import progfun.config.{CSV, Configuration, FileConfigInput, FileConfigOutput, JSON, OutputFormat, YAML}

class ConfigurationTest extends AnyFunSuite with EitherValues {

  val configTemplate =
    """app {
      |  input_mode = "%s"
      |  output_mode = "%s"
      |  output_format = "%s"
      |}
      |
      |file_input {
      |  path = "%s"
      |}
      |
      |file_output {
      |  path = "%s"
      |}""".stripMargin

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def configurationOrThrow(either: Either[Exception, Configuration]): Configuration = {
    either match {
      case Left(e) => throw e
      case Right(value) => value
    }
  }

  test("should create Config from string") {
    val configOverride =
      """app {
        |  input_mode = "file"
        |}""".stripMargin
    val config = ConfigFactory.parseString(configOverride)
      .withFallback(ConfigFactory.load())

    assertResult("file") {config.getString("app.input_mode")}
  }

  test("should create config file input and file output") {
    val configOverride = configTemplate.format(
      "file",
      "file",
      "csv",
      "src/test/resources/progfun/input.test",
      "src/test/resources/progfun/output.test"
    )
    val config = ConfigFactory.parseString(configOverride)
      .withFallback(ConfigFactory.load())

    assertResult((
      FileConfigInput(File("src/test/resources/progfun/input.test")),
      FileConfigOutput(File("src/test/resources/progfun/output.test"), CSV)
    )) {
      val c = configurationOrThrow(Configuration(config))
      (c.input, c.output)
    }
  }

  private val formats: List[(String, OutputFormat)] = List(("json", JSON), ("csv", CSV), ("yml", YAML), ("yaml", YAML))
  formats.foreach(format => {
    test(s"should create config file output format ${format._1}") {

      val configOverride = configTemplate.format(
        "file",
        "file",
        format._1,
        "src/test/resources/progfun/input.test",
        "src/test/resources/progfun/output.test"
      )
      val config = ConfigFactory.parseString(configOverride)
        .withFallback(ConfigFactory.load())

      assertResult((
        FileConfigInput(File("src/test/resources/progfun/input.test")),
        FileConfigOutput(File("src/test/resources/progfun/output.test"), format._2)
      )) {
        val c = configurationOrThrow(Configuration(config))
        (c.input, c.output)
      }
    }
  })


  test("should get left when missing app") {
    val config = ConfigFactory.empty()

    assertResult("Configuration [app.input_mode] is missing.") {
      Configuration(config).left.value.getMessage
    }
  }

}
