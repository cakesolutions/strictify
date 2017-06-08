package net.cakesolutions.strictify.scalapb.ops

import scala.util.Try

import com.trueaccord.scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}
import net.cakesolutions.strictify.core.{Binding, StrictifyError}

trait StrictTypeDeserializer[S] {
  def parseFrom(s: Array[Byte]): Either[StrictifyError, S]
}
object StrictTypeDeserializer {
  implicit def instance[S, L <: GeneratedMessage with Message[L]](
    implicit
    binding: Binding.Aux[S, L],
    companion: GeneratedMessageCompanion[L]
  ) = new StrictTypeDeserializer[S] {
    override def parseFrom(s: Array[Byte]): Either[StrictifyError, S] =
      Try(companion.parseFrom(s)).fold(e => Left(StrictifyError(e)), binding.strictify.apply)
  }
}

trait StrictTypeSerializer[S] {
  def toByteArray(value: S): Array[Byte]
}
object StrictTypeSerializer {
  implicit def instance[S, L <: GeneratedMessage with Message[L]](
    implicit
    binding: Binding.Aux[S, L]
  ) = new StrictTypeSerializer[S] {
    override def toByteArray(value: S): Array[Byte] =
      binding.loosen(value).toByteArray
  }
}

object SerializationOps {
  def parseFrom[S: StrictTypeDeserializer](s: Array[Byte]): Either[StrictifyError, S] =
    implicitly[StrictTypeDeserializer[S]].parseFrom(s)

  def toByteArray[S: StrictTypeSerializer](value: S): Array[Byte] =
    implicitly[StrictTypeSerializer[S]].toByteArray(value)
}