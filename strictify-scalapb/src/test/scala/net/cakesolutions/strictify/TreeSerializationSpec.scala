package net.cakesolutions.strictify

import com.example.model._
import eu.timepit.refined.auto._
import net.cakesolutions.strictify.scalapb.ops.{SerializationOps, StrictTypeDeserializer, StrictTypeSerializer}
import net.cakesolutions.strictify.scalapb.{BindingRefinedTypesImplicits, BindingSameScalaPbClassesImplicits}
import net.cakesolutions.strictify.testkit.BaseBindingSpec

class TreeSerializationSpec extends BaseBindingSpec with DefaultBindingConfig with BindingRefinedTypesImplicits with BindingSameScalaPbClassesImplicits {
  def checkRoundRobin[S : StrictTypeSerializer : StrictTypeDeserializer](value: S) =
    SerializationOps.parseFrom(SerializationOps.toByteArray(value)) shouldBe Right(value)

  "ScalaPb Serialization ops should" - {
    "round robin" - {
      "case classes" in checkRoundRobin(EmployeeData(1, "name", Some(99)))
      "sealed traits" in checkRoundRobin[BinaryTree](Leaf(1))
    }
  }
}