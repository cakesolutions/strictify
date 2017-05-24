package net.cakesolutions.strictify.scalapb

import com.trueaccord.scalapb._
import net.cakesolutions.strictify.core._
import shapeless._
import shapeless.ops.hlist

trait StrictifyScalaPbClassesImplicits extends StrictifyInstanceHelpers {

  implicit def strictifyProtoOneOfToSealedTrait[
    S,
    SRepr <: Coproduct,
    L <: GeneratedMessage with Message[L], // Generated wrapper
    LRepr <: HList,
    LOneOf <: GeneratedOneof,
    LOneOfRepr <: Coproduct
  ](
    implicit sGeneric: LabelledGeneric.Aux[S, SRepr],
    lWrapperGeneric: Generic.Aux[L, LRepr],
    matchlWrapperHead: hlist.IsHCons.Aux[LRepr, LOneOf, HNil], // Wrapper has to have only one field (the one-of)
    lOneOfGeneric: LabelledGeneric.Aux[LOneOf, LOneOfRepr],
    genericStrictifyInstance: Lazy[Strictify[SRepr, LOneOfRepr]]
  ) = instance[S, L] { looseObject =>
    val lRepr      = lWrapperGeneric.to(looseObject)
    val lOneOfRepr = lOneOfGeneric.to(lRepr.head)
    genericStrictifyInstance.value
      .apply(lOneOfRepr)
      .map(sGeneric.from)
  }

  implicit def strictifyWrappedCoproducts[
    SH,
    ST <: Coproduct,
    LH,
    LHInner <: GeneratedMessage with Message[LHInner],
    LT <: Coproduct
  ](
    implicit lWrapperGeneric: Generic.Aux[LH, LHInner :: HNil], // Wrapper has to have only one field (the Generated Message)
    strictifyHead: Lazy[Strictify[SH, LHInner]],
    strictifyTail: Strictify[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(lh) =>
      val lInner = lWrapperGeneric.to(lh).head
      strictifyHead.value.apply(lInner).map(Inl.apply)
    case Inr(t) =>
      strictifyTail.apply(t).map(Inr.apply)
  }

  implicit def strictifyWrappedCoproductsEmptyCase[
    SH,
    ST <: Coproduct,
    LH,
    LT <: Coproduct
  ](
    implicit sWrapperGeneric: Generic.Aux[SH, HNil], // Empty case must have no fields
    lWrapperGeneric: Generic.Aux[LH, HNil], // Empty case must have no fields
    strictifyTail: Strictify[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(_) =>
      Right(Inl(sWrapperGeneric.from(HNil)))
    case Inr(t) =>
      strictifyTail.apply(t).map(Inr.apply)
  }
}
trait LoosenScalaPbClassesImplicits extends LoosenInstanceHelpers {
  implicit def loosenSealedTraitToProtoOneOf[
    S,
    SRepr <: Coproduct,
    L <: GeneratedMessage with Message[L], // Generated wrapper
    LOneOf <: GeneratedOneof,
    LOneOfRepr <: Coproduct
  ](
    implicit sGeneric: LabelledGeneric.Aux[S, SRepr],
    lWrapperGeneric: Generic.Aux[L, LOneOf :: HNil], // Wrapper has to have only one field (the one-of)
    lOneOfGeneric: LabelledGeneric.Aux[LOneOf, LOneOfRepr],
    genericStrictifyInstance: Lazy[Loosen[SRepr, LOneOfRepr]]
  ) = instance[S, L] { strictObject =>
    lWrapperGeneric.from(
      lOneOfGeneric.from(
        genericStrictifyInstance.value
          .apply(sGeneric.to(strictObject))
      ) :: HNil
    )
  }

  implicit def loosenWrappedCoproducts[
    SH,
    ST <: Coproduct,
    LH,
    LHInner <: GeneratedMessage with Message[LHInner],
    LT <: Coproduct
  ](
    implicit lWrapperGeneric: Generic.Aux[LH, LHInner :: HNil], // Wrapper has to have only one field (the Generated Message)
    loosenHead: Lazy[Loosen[SH, LHInner]],
    loosenTail: Loosen[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(sh) =>
      Inl(lWrapperGeneric.from(loosenHead.value.apply(sh) :: HNil))
    case Inr(st) =>
      Inr(loosenTail.apply(st))
  }

  implicit def loosenWrappedCoproductsEmptyCase[
    SH,
    ST <: Coproduct,
    LH,
    LT <: Coproduct
  ](
    implicit sWrapperGeneric: Generic.Aux[SH, HNil], // Empty case must have no fields
    lWrapperGeneric: Generic.Aux[LH, HNil], // Empty case must have no fields
    loosenTail: Loosen[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(_) =>
      Inl(lWrapperGeneric.from(HNil))
    case Inr(st) =>
      Inr(loosenTail.apply(st))
  }

  implicit def loosenCoproducts[SH, ST <: Coproduct, LH, LT <: Coproduct](
    implicit loosenHead: Lazy[Loosen[SH, LH]],
    loosenTail: Loosen[ST, LT]
  ) = instance[SH :+: ST, LH :+: LT] {
    case Inl(h) => Inl(loosenHead.value.apply(h))
    case Inr(t) => Inr(loosenTail.apply(t))
  }
}
trait BindingSameScalaPbClassesImplicits
    extends StrictifyScalaPbClassesImplicits
    with LoosenScalaPbClassesImplicits
