package net.cakesolutions.strictify.scalapb

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import net.cakesolutions.strictify.structured.{BindingSimilarCaseClassesImplicits, BindingSimilarSealedTraitsImplicits}
import net.cakesolutions.strictify.testkit.BaseBindingSpec

class StrictifyBindingSpec extends BaseBindingSpec with BindingSameRefinedTypesImplicits with BindingSimilarCaseClassesImplicits with BindingSimilarSealedTraitsImplicits {
  "Refined Bindings should" - {
    "Support basic cases" - {
      "Ints" in check[Int Refined Positive, Int](1, 1)
      "Strings" in check[String Refined NonEmpty, String]("b", "b")
      "Complex structures" in {
        sealed trait StrictTree
        case class StrictLeaf(value: Int Refined Positive) extends StrictTree
        case class StrictNode(left: StrictTree, right: StrictTree)
          extends StrictTree

        sealed trait LooseTree
        case class LooseLeaf(value: Option[Int]) extends LooseTree
        case class LooseNode(
           left: Option[LooseTree],
           right: Option[LooseTree]
         ) extends LooseTree

        check(
          StrictNode(
            StrictLeaf(1),
            StrictNode(
              StrictLeaf(2),
              StrictLeaf(3)
            )
          ),
          LooseNode(
            Some(LooseLeaf(Some(1))),
            Some(
              LooseNode(
                Some(LooseLeaf(Some(2))),
                Some(LooseLeaf(Some(3)))
              )
            )
          )
        )
      }
    }
  }
}