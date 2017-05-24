package net.cakesolutions.strictify.scalapb

import eu.timepit.refined.api.{ RefType, Refined, Validate }
import net.cakesolutions.strictify.core._

trait StrictifyRefinedTypesImplicits extends StrictifyInstanceHelpers {
  implicit def stricitifyToRefined[T, P](implicit v: Validate[T, P]) =
    instance[T Refined P, T](
      RefType.applyRef[T Refined P](_).left.map(StrictifyError.apply)
    )
}
trait LoosenRefinedTypesImplicits extends LoosenInstanceHelpers {
  implicit def loosenToRefined[T, P](implicit v: Validate[T, P]) =
    instance[T Refined P, T](_.value)
}
trait BindingSameRefinedTypesImplicits
    extends StrictifyRefinedTypesImplicits
    with LoosenRefinedTypesImplicits
