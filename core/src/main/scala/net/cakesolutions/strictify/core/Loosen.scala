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
  implicit def loosenToSome[S, L](implicit innerInstance: Loosen[S, L]) =
    instance[S, Some[L]](s => Some(innerInstance(s)))

  implicit def loosenToOption[S, L](implicit innerInstance: Loosen[S, L]) =
    instance[S, Option[L]](s => Some(innerInstance(s)))

  implicit def loosenSToeqItems[S, L](implicit itemInstance: Loosen[S, L]) =
    instance[Seq[S], Seq[L]](_.map(itemInstance.apply))

  implicit def passthroughBothOptions[S, L](
    implicit innerInstance: Loosen[S, L]
  ) =
    instance[Option[S], Option[L]](l => l.map(innerInstance.apply))

  implicit def passthroughSameType[T] = instance[T, T](identity)
}

trait LoosenInstanceHelpers {
  def instance[S, L](impl: S => L) = new Loosen[S, L] {
    override def apply(value: S): L = impl(value)
  }
}
