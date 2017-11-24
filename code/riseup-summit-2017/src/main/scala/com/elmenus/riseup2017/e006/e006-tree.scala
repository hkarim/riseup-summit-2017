package com.elmenus.riseup2017.e006

import scala.math.Ordering


sealed trait Tree[+A]

case class Node[+A](data: A, left: Tree[A], right: Tree[A]) extends Tree[A]

case class Leaf[+A](data: A) extends Tree[A]

case object Empty extends Tree[Nothing]

object Tree {

  def empty: Empty.type = Empty

  def of[A](data: A): Tree[A] = Leaf(data)

  def insert[A](tree: Tree[A], data: A)(ordering: Ordering[A]): Tree[A] = tree match {

    case Empty    =>
      Leaf(data)

    case Leaf(a)  =>
      if (ordering.compare(data, a) < 0)
        Node(a, Leaf(data), Empty)
      else
        Node(a, Empty, Leaf(data))

    case Node(a, l, r) =>
      if (ordering.compare(data, a) < 0)
        Node(a, Tree.insert(l, data)(ordering), r)
      else
        Node(a, l, Tree.insert(r, data)(ordering))
  }

  def inOrder[A](tree: Tree[A]): List[A] = tree match {
    case Empty         => List.empty[A]
    case Leaf(a)       => List(a)
    case Node(a, l, r) => inOrder(l) ++ List(a) ++ inOrder(r)
  }

}


object E006App {

  def insert(tree: Tree[Int], data: Int): Tree[Int] =
    Tree.insert[Int](tree, data)(Ordering.Int)

  def insert(tree: Tree[Int], l: List[Int]): Tree[Int] = tree match {

    case Empty => l match {
      case Nil      => Empty
      case h :: Nil => Leaf(h)
      case h :: t   => insert(Leaf(h), t)
    }

    case n@Leaf(_) => l match {
      case Nil      => n
      case h :: Nil => insert(n, h)
      case h :: t   => insert(insert(n, h), t)
    }

    case n@Node(_, _, _) => l match {
      case Nil      => n
      case h :: Nil => insert(n, h)
      case h :: t   => insert(insert(n, h), t)
    }
  }



  def main(args: Array[String]): Unit = {

    val tree = insert(Tree.empty, List(5,6,3,0,9,1,13,5,-1))

    println(Tree.inOrder(tree))
  }



}





















