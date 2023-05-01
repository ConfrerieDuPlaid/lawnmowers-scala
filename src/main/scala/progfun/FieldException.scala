package progfun

case class FieldException (private val message:String) extends Exception(message) {}

object FieldException {
  def negativeLimits(limits: (Int,Int)): FieldException = new FieldException(s"Fields limits must be strictly positive. Given = $limits")
}
