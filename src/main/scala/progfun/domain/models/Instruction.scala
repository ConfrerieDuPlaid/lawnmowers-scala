package progfun.domain.models

/**
 * Scala 2 compiler is not able to infer the correct type
 * So List(RotateLeft, MoveForward) result to a compilation error Inferred type containing Product
 * -> https://www.baeldung.com/scala/product-with-serializable
 */
sealed trait Instruction extends Product with Serializable {
  def execute(mower: Lawnmower): Lawnmower
}

case object RotateLeft extends Instruction {
  override def execute(mower: Lawnmower): Lawnmower = {
    mower.copy(orientation = mower.orientation match {
      case North => West
      case West => South
      case South => East
      case East => North
      case _ => mower.orientation
    })
  }
}
case object RotateRight extends Instruction {
  override def execute(mower: Lawnmower): Lawnmower = {
    mower.copy(orientation = mower.orientation match {
      case North => East
      case East => South
      case South => West
      case West => North
      case _ => mower.orientation
    })
  }
}
case object MoveForward extends Instruction {
  override def execute(mower: Lawnmower): Lawnmower = mower.copy(position = mower.orientation.of(mower.position))
}
