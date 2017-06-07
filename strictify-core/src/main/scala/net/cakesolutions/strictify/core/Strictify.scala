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
