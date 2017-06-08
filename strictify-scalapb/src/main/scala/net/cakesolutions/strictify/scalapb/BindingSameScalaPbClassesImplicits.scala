package net.cakesolutions.strictify.scalapb

import com.trueaccord.scalapb._
import net.cakesolutions.strictify.core._
import net.cakesolutions.strictify.structured.BindingSimilarSealedTraitsImplicits
import shapeless._

trait StrictifyScalaPbClassesImplicits extends StrictifyInstanceHelpers {
  implicit def strictifyProtoOneOfToSealedTrait[
    S,
    L <: GeneratedMessage with Message[L], // Generated wrapper
    LOneOf <: GeneratedOneof
  ](
    implicit
    lWrapperGeneric: Lazy[Generic.Aux[L, LOneOf :: HNil]], // Wrapper has to have only one field (the one-of)
    strictifyWrapped: Lazy[Strictify[S, LOneOf]]
  ) = instance[S, L] { looseObject =>
    val wrapped = lWrapperGeneric.value.to(looseObject).head
    strictifyWrapped.value.apply(wrapped)
  }

  implicit def strictifyProtoOneOfWrapper[
    S,
    L <: GeneratedOneof, // Generated wrapper
    LWrapped <: GeneratedMessage with Message[LWrapped]
  ](
     implicit
     lWrapperGeneric: Lazy[Generic.Aux[L, LWrapped :: HNil]], // Wrapper has to have only one field (the one-of)
     strictifyWrapped: Lazy[Strictify[S, LWrapped]]
   ) = instance[S, L] { looseObject =>
    val wrapped = lWrapperGeneric.value.to(looseObject).head
    strictifyWrapped.value.apply(wrapped)
  }
}
trait LoosenScalaPbClassesImplicits extends LoosenInstanceHelpers {
  implicit def loosenSealedTraitToProtoOneOf[
    S,
    L <: GeneratedMessage with Message[L], // Generated wrapper
    LOneOf <: GeneratedOneof
  ] (
    implicit
    lWrapperGeneric: Lazy[Generic.Aux[L, LOneOf :: HNil]], // Wrapper has to have only one field (the one-of)
    loosenWrapped: Lazy[Loosen[S, LOneOf]]
  ) = instance[S, L] { strictObject =>
    lWrapperGeneric.value.from(loosenWrapped.value.apply(strictObject) :: HNil)
  }

  implicit def loosenProtoOneOfWrapper[
  S,
  L <: GeneratedOneof, // Generated wrapper
  LWrapped <: GeneratedMessage with Message[LWrapped]
  ] (
      implicit
      lWrapperGeneric: Lazy[Generic.Aux[L, LWrapped :: HNil]], // Wrapper has to have only one field (the one-of)
      loosenWrapped: Lazy[Loosen[S, LWrapped]]
    ) = instance[S, L] { strictObject =>
    lWrapperGeneric.value.from(loosenWrapped.value.apply(strictObject) :: HNil)
  }
}
trait BindingSameScalaPbClassesImplicits
    extends BindingSimilarSealedTraitsImplicits
    with StrictifyScalaPbClassesImplicits
    with LoosenScalaPbClassesImplicits
