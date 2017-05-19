package net.cakesolutions.strictify.structured

import net.cakesolutions.strictify.core._
import shapeless._

trait StrictifySimilarSealedTraitsImplicits extends StrictifyInstanceHelpers {
  implicit def strictifyCoproducts[SH, ST <: Coproduct, LH, LT <: Coproduct](
    implicit strictifyHead: Lazy[Strictify[SH, LH]],
    strictifyTail: Strictify[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(h) => strictifyHead.value.apply(h).map(Inl.apply)
    case Inr(t) => strictifyTail.apply(t).map(Inr.apply)
  }

  implicit def strictifyGenericObjects[
    S,
    SRepr <: Coproduct,
    L,
    LRepr <: Coproduct
  ](
    implicit strictTypeGeneric: Generic.Aux[S, SRepr],
    looseTypeGeneric: Generic.Aux[L, LRepr],
    genericStrictifyInstance: Lazy[Strictify[SRepr, LRepr]]
  ) = instance[S, L] { looseObject =>
    genericStrictifyInstance.value
      .apply(looseTypeGeneric.to(looseObject))
      .map(strictTypeGeneric.from)
  }
}

trait LoosenSimilarSealedTraitsImplicits extends LoosenInstanceHelpers {
  implicit def loosenCoproducts[SH, ST <: Coproduct, LH, LT <: Coproduct](
    implicit loosenHead: Lazy[Loosen[SH, LH]],
    loosenTail: Loosen[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(h) => Inl(loosenHead.value.apply(h))
    case Inr(t) => Inr(loosenTail.apply(t))
  }

  implicit def loosenGenericObjects[
    S,
    L,
    SRepr <: Coproduct,
    LRepr <: Coproduct
  ](
    implicit strictTypeGeneric: Generic.Aux[S, SRepr],
    looseTypeGeneric: Generic.Aux[L, LRepr],
    genericLoosenInstance: Lazy[Loosen[SRepr, LRepr]]
  ) = instance[S, L] { strictObject =>
    looseTypeGeneric.from(
      genericLoosenInstance.value
        .apply(strictTypeGeneric.to(strictObject))
    )
  }

}

trait BindingSimilarSealedTraitsImplicits
    extends StrictifySimilarSealedTraitsImplicits
    with LoosenSimilarSealedTraitsImplicits
