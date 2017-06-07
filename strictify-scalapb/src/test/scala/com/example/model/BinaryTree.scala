package com.example.model

import net.cakesolutions.strictify.core.Binding
import com.example.protocol.{tree => proto}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

sealed trait BinaryTree

case object Empty extends BinaryTree // For the moment we have to match ScalaPb
case class Leaf(value: Int Refined Positive) extends BinaryTree
case class Branch(left: BinaryTree, right: BinaryTree) extends BinaryTree

object BinaryTree {
  implicit val protocolBinding = Binding.instance[BinaryTree, proto.BinaryTree]
}
