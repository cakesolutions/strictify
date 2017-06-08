package net.cakesolutions.strictify.structured

import net.cakesolutions.strictify.DefaultBindingConfig
import net.cakesolutions.strictify.core.Binding
import net.cakesolutions.strictify.testkit.BaseBindingSpec

case class Strict(a: Int, b: String, c: Seq[Boolean])
case class Loose(a: Option[Int], b: Option[String], c: Seq[Option[Boolean]])

class StrictifySimilarCaseClassesSpec
    extends BaseBindingSpec
    with DefaultBindingConfig
    with BindingSimilarSealedTraitsImplicits {
  "Case Classes Bindings should support" - {
    "simple case classes" in {
      check(
        Strict(1, "b", Seq(false)),
        Loose(Some(1), Some("b"), Seq(Some(false)))
      )
    }
    "Options on Case classes" in {
      check[Option[Strict], Option[Loose]](
        Some(Strict(1, "b", Seq(false))),
        Some(Loose(Some(1), Some("b"), Seq(Some(false))))
      )
    }
    "Nested structures" in {
      case class Strict1(a: Strict)
      case class Loose1(a: Option[Loose])

      check(
        Strict1(Strict(1, "b", Seq(false))),
        Loose1(Some(Loose(Some(1), Some("b"), Seq(Some(false)))))
      )
    }
    "Sealed traits" in {
      sealed trait STrait
      final case class SInt(a: Int)       extends STrait
      final case class SString(b: String) extends STrait

      sealed trait LTrait
      final case class LInt(a: Option[Int])       extends LTrait
      final case class LString(b: Option[String]) extends LTrait

      check[STrait, LTrait](
        SInt(5),
        LInt(Some(5))
      )
    }
    "Recursive structures" in {
      sealed trait StrictTree[T]
      case class StrictLeaf[T](value: T) extends StrictTree[T]
      case class StrictNode[T](left: StrictTree[T], right: StrictTree[T])
          extends StrictTree[T]

      sealed trait LooseTree[T]
      case class LooseLeaf[T](value: Option[T]) extends LooseTree[T]
      case class LooseNode[T](
        left: Option[LooseTree[T]],
        right: Option[LooseTree[T]]
      ) extends LooseTree[T]

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
    "binding" in {
      implicit val binding = Binding.instance[Strict, Loose]
      checkBinding(
        Strict(1, "b", Seq(false)),
        Loose(Some(1), Some("b"), Seq(Some(false)))
      )
    }
  }
}
