package progfun

final case class ParsingException (private val message:String) extends Exception(message) {
}

