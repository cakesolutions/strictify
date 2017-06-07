package com.example

import eu.timepit.refined.auto._
import com.example.model._
import net.cakesolutions.strictify.scalapb.ops.SerializationOps._

object Main extends App {
  val obj1 = EmployeeData(1, "name", Some(99))
  val bytes1 = toByteArray(obj1)
  val parsedObj1 = parseFrom[EmployeeData](bytes1)
  println(parsedObj1)

  val obj2: BinaryTree = Branch(Leaf(1), Leaf(2))
  val bytes2 = toByteArray(obj2)
  val parsedObj2 = parseFrom[BinaryTree](bytes2)
  println(parsedObj2)
}