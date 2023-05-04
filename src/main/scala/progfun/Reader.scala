package progfun

trait Reader[T] {
  def read(input: T): Option[String]
}
