package net.cakesolutions.strictify.structured

import net.cakesolutions.strictify.core._
import shapeless._
import shapeless.labelled._

trait StrictifySimilarCaseClassesImplicits extends StrictifyInstanceHelpers {
  implicit def strictifyLabelledGenericObjects[
    S,
    L,
    SRepr <: HList,
    LRepr <: HList
  ](
    implicit strictTypeGeneric: LabelledGeneric.Aux[S, SRepr],
    looseTypeGeneric: LabelledGeneric.Aux[L, LRepr],
    genericStrictifyInstance: Lazy[Strictify[SRepr, LRepr]]
  ) = instance[S, L] { looseObject =>
    genericStrictifyInstance.value
      .apply(looseTypeGeneric.to(looseObject))
      .map(strictTypeGeneric.from)
  }

  implicit def strictifyHCons[HKey, SH, ST <: HList, LH, LT <: HList](
    implicit headKeyNameWitness: Witness.Aux[HKey], // Ensures both classes have the same fields names
    strictifyHead: Lazy[Strictify[SH, LH]],
    strictifyTail: Strictify[ST, LT]
  ) = instance[FieldType[HKey, SH] :: ST, FieldType[HKey, LH] :: LT] {
    looseHList =>
      for {
        head <- strictifyHead.value.apply(looseHList.head)
        tail <- strictifyTail.apply(looseHList.tail)
      } yield field[HKey](head) :: tail
  }
}

trait LoosenSimilarCaseClassesImplicits extends LoosenInstanceHelpers {
  implicit def loosenLabelledGenericObjects[
    S,
    L,
    SRepr <: HList,
    LRepr <: HList
  ](
    implicit strictTypeGeneric: LabelledGeneric.Aux[S, SRepr],
    looseTypeGeneric: LabelledGeneric.Aux[L, LRepr],
    genericLoosenInstance: Lazy[Loosen[SRepr, LRepr]]
  ) = instance[S, L] { strictObject =>
    looseTypeGeneric.from(
      genericLoosenInstance.value
        .apply(strictTypeGeneric.to(strictObject))
    )
  }

  implicit def loosenHCons[HKey, SH, ST <: HList, LH, LT <: HList](
    implicit headKeyNameWitness: Witness.Aux[HKey], // Ensures both class have the same fields names
    loosenHead: Lazy[Loosen[SH, LH]],
    loosenTail: Loosen[ST, LT]
  ) = instance[FieldType[HKey, SH] :: ST, FieldType[HKey, LH] :: LT] {
    looseHList =>
      val head = loosenHead.value.apply(looseHList.head)
      val tail = loosenTail.apply(looseHList.tail)
      field[HKey](head) :: tail
  }
}

trait BindingSimilarCaseClassesImplicits
    extends StrictifySimilarCaseClassesImplicits
    with LoosenSimilarCaseClassesImplicits
