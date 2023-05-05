package progfun.domain.models

sealed trait Orientation {
  def of(position: Position): Position
}
case object North extends Orientation {
  override def of(position: Position): Position = position.copy(y = position.y + 1)
}

case object East extends Orientation {
  override def of(position: Position): Position = position.copy(x = position.x + 1)
}

case object West extends Orientation {
  override def of(position: Position): Position = position.copy(x = position.x - 1)
}

case object South extends Orientation {
  override def of(position: Position): Position = position.copy(y = position.y - 1)
}
