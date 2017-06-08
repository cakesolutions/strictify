package net.cakesolutions.strictify.core

trait StrictifyOptionsImplicits extends StrictifyInstanceHelpers {
  implicit def strictifySome[S, L](
    implicit innerInstance: Strictify[S, L]
  ) = instance[S, Some[L]](s => innerInstance(s.get))

  implicit def strictifNone[T] = alwaysErrorInstance[T, None.type]("Option value is unexpectedly empty")

  implicit def strictifyOptions[S, L](
    implicit someInstance: Strictify[S, Some[L]],
    noneInstance: Strictify[S, None.type]
  ) = instance[S, Option[L]] {
    case some @ Some(_) => someInstance(some)
    case None        => noneInstance(None)
  }

  implicit def strictifyBothOptions[S, L](
    implicit innerInstance: Strictify[S, L]
  ) =
    instance[Option[S], Option[L]](
      s =>
        s.map(innerInstance.apply)
          .map(_.map(Some.apply))
          .getOrElse(Right(None))
    )
}

trait LoosenOptionsImplicits extends LoosenInstanceHelpers {
  implicit def loosenToSome[S, L](implicit innerInstance: Loosen[S, L]) =
    instance[S, Some[L]](s => Some(innerInstance(s)))

  implicit def loosenToOption[S, L](implicit innerInstance: Loosen[S, L]) =
    instance[S, Option[L]](s => Some(innerInstance(s)))

  implicit def loosenBothOptions[S, L](
    implicit innerInstance: Loosen[S, L]
  ) =
    instance[Option[S], Option[L]](l => l.map(innerInstance.apply))
}

trait BindingOptionsImplicits
    extends StrictifyOptionsImplicits
    with LoosenOptionsImplicits
