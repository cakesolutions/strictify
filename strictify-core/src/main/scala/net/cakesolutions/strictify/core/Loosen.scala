package net.cakesolutions.strictify.core

/**
  * Converts from a strict type to a more loose version
  * T -> Option[T]
  * PositiveNumber -> Int
  */
trait Loosen[Strict, Loose] {
  def apply(value: Strict): Loose
}

object Loosen extends LoosenInstanceHelpers {
  implicit def passthroughSameType[T] = instance[T, T](identity)
}

trait LoosenInstanceHelpers {
  def instance[S, L](impl: S => L) = new Loosen[S, L] {
    override def apply(value: S): L = impl(value)
  }
}
