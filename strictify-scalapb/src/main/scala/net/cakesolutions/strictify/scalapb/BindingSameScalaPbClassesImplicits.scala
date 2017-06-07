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

//
//  implicit def strictifyCoproducts[SH, ST <: Coproduct, LH, LT <: Coproduct](
//    implicit strictifyHead: Lazy[Strictify[SH, LH]],
//    strictifyTail: Strictify[ST, LT]
//  ) = instance[SH :+: ST, LH :+: LT] {
//    case Inl(h) => strictifyHead.value.apply(h).map(Inl.apply)
//    case Inr(t) => strictifyTail.apply(t).map(Inr.apply)
//  }
//
////  implicit def strictifyWrappedCoproducts[
////    SH,
////    ST <: Coproduct,
////    LH,
////    LHRepr <: HList,
////    LHInner, //  <: GeneratedMessage with Message[LHInner]
////    LT <: Coproduct
////  ]
//////  (
//////    implicit lWrapperGeneric: Generic.Aux[LH, LHRepr], // Wrapper has to have only one field (the Generated Message)
//////    matchLHead: hlist.IsHCons.Aux[LHRepr, LHInner, HNil], // Wrapper has to have only one field (the one-of)
////////    strictifyHead: Lazy[Strictify[SH, LHInner]],
////////    strictifyTail: Strictify[ST, LT]
//////  )
////  = instance[SH :+: ST, LH :+: LT] {
//////    case Inl(lh) =>
//////      val lInner = lWrapperGeneric.to(lh).head
//////      strictifyHead.value.apply(lInner).map(Inl.apply)
//////    case Inr(t) =>
//////      strictifyTail.apply(t).map(Inr.apply)
////    ???
////  }
////
////  implicit def strictifyWrappedCoproductsEmptyCase[
////    SH,
////    ST <: Coproduct,
////    LH,
////    LT <: Coproduct
////  ](
////    implicit sWrapperGeneric: Generic.Aux[SH, HNil], // Empty case must have no fields
////    lWrapperGeneric: Generic.Aux[LH, HNil], // Empty case must have no fields
////    strictifyTail: Strictify[ST, LT]
////  ) = instance[SH :+: ST, LH :+: LT] {
////    case Inl(_) =>
////      Right(Inl(sWrapperGeneric.from(HNil)))
////    case Inr(t) =>
////      strictifyTail.apply(t).map(Inr.apply)
////  }
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

//
//  implicit def loosenWrappedCoproducts[
//    SH,
//    ST <: Coproduct,
//    LH,
//    LHInner <: GeneratedMessage with Message[LHInner],
//    LT <: Coproduct
//  ]
////  (
////    implicit lWrapperGeneric: Generic.Aux[LH, LHInner :: HNil], // Wrapper has to have only one field (the Generated Message)
////    loosenHead: Lazy[Loosen[SH, LHInner]],
////    loosenTail: Loosen[ST, LT]
////  )
//  = instance[SH :+: ST, LH :+: LT] {
////    case Inl(sh) =>
////      Inl(lWrapperGeneric.from(loosenHead.value.apply(sh) :: HNil))
////    case Inr(st) =>
////      Inr(loosenTail.apply(st))
//    ???
//  }
//
//  implicit def loosenWrappedCoproductsEmptyCase[
//    SH,
//    ST <: Coproduct,
//    LH,
//    LT <: Coproduct
//  ](
//    implicit sWrapperGeneric: Generic.Aux[SH, HNil], // Empty case must have no fields
//    lWrapperGeneric: Generic.Aux[LH, HNil], // Empty case must have no fields
//    loosenTail: Loosen[ST, LT]
//  ) = instance[SH :+: ST, LH :+: LT] {
//    case Inl(_) =>
//      Inl(lWrapperGeneric.from(HNil))
//    case Inr(st) =>
//      Inr(loosenTail.apply(st))
//  }
//
//  implicit def loosenCoproducts[SH, ST <: Coproduct, LH, LT <: Coproduct](
//    implicit loosenHead: Lazy[Loosen[SH, LH]],
//    loosenTail: Loosen[ST, LT]
//  ) = instance[SH :+: ST, LH :+: LT] {
//    case Inl(h) => Inl(loosenHead.value.apply(h))
//    case Inr(t) => Inr(loosenTail.apply(t))
//  }
}
trait BindingSameScalaPbClassesImplicits
    extends BindingSimilarSealedTraitsImplicits
    with StrictifyScalaPbClassesImplicits
    with LoosenScalaPbClassesImplicits
