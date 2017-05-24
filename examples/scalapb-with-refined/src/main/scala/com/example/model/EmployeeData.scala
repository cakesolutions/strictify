package com.example.model

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import net.cakesolutions.strictify.core.Binding

case class EmployeeData(
  id: Int,
  name: String Refined NonEmpty,
  age: Option[Int Refined Positive]
)
object EmployeeData {
  implicit val protocolBinding = Binding.instance[EmployeeData, com.example.protocol.employee.EmployeeData]
}
