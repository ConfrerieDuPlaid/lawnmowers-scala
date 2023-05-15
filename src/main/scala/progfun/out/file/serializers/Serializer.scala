package progfun.out.file.serializers

trait Serializer[From] {
  def serialize(from: From): String
}
