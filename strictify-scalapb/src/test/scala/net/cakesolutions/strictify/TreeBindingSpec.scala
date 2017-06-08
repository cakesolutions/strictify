package net.cakesolutions.strictify

import com.example.model
import com.example.protocol
import net.cakesolutions.strictify.scalapb.{BindingRefinedTypesImplicits, BindingSameScalaPbClassesImplicits}
import net.cakesolutions.strictify.testkit.BaseBindingSpec
import eu.timepit.refined.auto._

class TreeBindingSpec extends BaseBindingSpec with DefaultBindingConfig with BindingRefinedTypesImplicits with BindingSameScalaPbClassesImplicits {

  "ScalaPb Binding should" - {
    "convert" - {
      "case classes" in checkBinding[model.EmployeeData, protocol.employee.EmployeeData](
        model.EmployeeData(1, "name", Some(99)),
        protocol.employee.EmployeeData(1, "name", Some(99))
      )

      "sealed traits" in checkBinding[model.BinaryTree, protocol.tree.BinaryTree](
        model.Leaf(1),
        protocol.tree.BinaryTree(protocol.tree.BinaryTree.Value.Leaf(protocol.tree.Leaf(1)))
      )
    }
  }
}