package net.cakesolutions.strictify

import com.example.model
import com.example.protocol.{tree => proto}
import net.cakesolutions.strictify.scalapb.{BindingRefinedTypesImplicits, BindingSameScalaPbClassesImplicits}
import net.cakesolutions.strictify.testkit.BaseBindingSpec
import eu.timepit.refined.auto._

class TreeBindingSpec extends BaseBindingSpec with DefaultBindingConfig with BindingRefinedTypesImplicits with BindingSameScalaPbClassesImplicits {

  "A" - {
    "B" - {
      "C" in checkBinding[model.BinaryTree, proto.BinaryTree](
        model.Leaf(1),
        proto.BinaryTree(proto.BinaryTree.Value.Leaf(proto.Leaf(1)))
      )
    }
  }
}