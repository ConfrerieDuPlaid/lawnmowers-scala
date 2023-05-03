package progfun

/**
 * Scala 2 compiler is not able to infer the correct type
 * So List(RotateLeft, MoveForward) result to a compilation error Inferred type containing Product
 * -> https://www.baeldung.com/scala/product-with-serializable
 */
sealed trait Instruction extends Product with Serializable

case object RotateLeft extends Instruction {}
case object RotateRight extends Instruction {}
case object MoveForward extends Instruction {}