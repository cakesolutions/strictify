package com.example.model

import net.cakesolutions.strictify.core.Binding
import com.example.protocol.{tree => proto}

sealed trait BinaryTree

case object Empty extends BinaryTree {
  implicit val protocolBinding = Binding.instance[Empty.type, proto.BinaryTree.Value.Empty.type]
}
case class Leaf(value: Int) extends BinaryTree
case object Leaf {
  implicit val protocolBinding = Binding.instance[Leaf, proto.Leaf]
}

case class Branch(left: BinaryTree, right: BinaryTree) extends BinaryTree
case object Branch {
//  implicit val protocolBinding = Binding.instance[Branch, proto.Branch]
}

case object BinaryTree {
  implicit val protocolBinding = Binding.instance[BinaryTree, proto.BinaryTree]
}
