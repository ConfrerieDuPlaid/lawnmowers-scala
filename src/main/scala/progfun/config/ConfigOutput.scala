package progfun.config

import better.files.File

sealed trait ConfigOutput

case class FileConfigOutput(file: File, format: OutputFormat) extends ConfigOutput
case class CliConfigOutput(format: OutputFormat) extends ConfigOutput