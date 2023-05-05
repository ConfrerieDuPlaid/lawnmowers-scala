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
    mower
  }
}
case object RotateRight extends Instruction {
  override def execute(mower: Lawnmower): Lawnmower = {
    mower
  }
}
case object MoveForward extends Instruction {
  override def execute(mower: Lawnmower): Lawnmower = {
    val oldPosition = mower.position
    mower.copy(position = oldPosition.copy(y = oldPosition.y + 1))
  }
}
