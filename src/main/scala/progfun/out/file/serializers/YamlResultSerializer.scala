/*package progfun.out.file
import progfun.domain.ports.out.LawnMowedResult

object YamlResultSerializer extends ResultSerializer {
  override def serialize(lawnMowedResult: LawnMowedResult): String = {
    val lawn = lawnMowedResult.lawn
    val (x, y) = (lawn.width - 1, lawn.height - 1)
    s"""limite:
       |  x: ${x.toString}
       |  y: ${y.toString}
       |tondeuses: []""".stripMargin

  }
}*/
