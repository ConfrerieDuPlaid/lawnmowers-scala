package progfun

import better.files.File

case object FileReader extends Reader[String] {
  override def read(path: String): Option[String] = {
    val file = File(path)
    if(file.isRegularFile) Some(file.contentAsString)
    else None
  }
}
