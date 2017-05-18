package net.cakesolutions.strictify.core

trait StrictifyError {
  def message: String
}
object StrictifyError {
  case class StringBased(message: String) extends StrictifyError
  def apply(msg: String): StrictifyError = StringBased(msg)
}

/**
  * Converts from a type to a more strict version
  * Option[T] -> T
  * Int -> PositiveNumber
  */
trait Strictify[Strict, Loose] {
  def apply(value: Loose): Either[StrictifyError, Strict]
}

object Strictify extends StrictifyInstanceHelpers {
  implicit def strictifySome[S, L](
    implicit innerInstance: Strictify[S, L]
  ) = instance[S, Some[L]](s => innerInstance(s.get))

//  implicit def strictifNone[T] = alwaysErrorInstance[T, None.type]("Option value is unexpectedly empty")

  implicit def strictifyOptions[S, L](
    implicit innerInstance: Strictify[S, L]
  ) = instance[S, Option[L]] {
    case Some(value) => innerInstance(value)
    case None        => error("Option value is unexpectedly empty")
  }

  implicit def strictifySeqItems[S, L](
    implicit itemInstance: Strictify[S, L]
  ) =
    instance[Seq[S], Seq[L]](
      _.map(itemInstance.apply)
        .foldRight[Either[StrictifyError, Seq[S]]](Right(Seq.empty)) {
          case (_, Left(e))                => Left(e)
          case (Left(e), _)                => Left(e)
          case (Right(item), Right(items)) => Right(item +: items)
        }
    )

//  implicit def passthroughSameMonad[M[_], S, L] = // Cats??
//    instance[M[S], M[L]](Right.apply)

  implicit def passthroughBothOptions[S, L](
    implicit innerInstance: Strictify[S, L]
  ) =
    instance[Option[S], Option[L]](
      s =>
        s.map(innerInstance.apply)
          .map(_.map(Some.apply))
          .getOrElse(Right(None))
    )

  implicit def passthroughSameType[T] = instance[T, T](Right.apply)
}

trait StrictifyInstanceHelpers {
  def instance[S, L](impl: L => Either[StrictifyError, S]) =
    new Strictify[S, L] {
      override def apply(value: L): Either[StrictifyError, S] = impl(value)
    }

  def alwaysSuccessInstance[S, L](impl: L => S) =
    instance[S, L](impl.andThen(Right.apply))

  def alwaysErrorInstance[S, L](errorMsg: String) =
    instance[S, L](_ => error(errorMsg))

  protected def error(msg: String) = Left(StrictifyError(msg))
}
