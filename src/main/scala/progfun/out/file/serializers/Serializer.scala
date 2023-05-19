package progfun.out.file.serializers

trait Serializer[A] {
  def serialize(from: A): String
}
