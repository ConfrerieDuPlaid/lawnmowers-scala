package progfun.config

import better.files.File

sealed trait ConfigInput

case class FileConfigInput(file: File) extends ConfigInput
