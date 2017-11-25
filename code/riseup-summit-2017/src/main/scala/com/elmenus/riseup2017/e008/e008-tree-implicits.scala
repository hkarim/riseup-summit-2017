package com.elmenus.riseup2017.e008

import scala.math.Ordering
import scala.util.Random


sealed trait Tree[+A]

case class Node[+A](data: A, left: Tree[A], right: Tree[A]) extends Tree[A]

case class Leaf[+A](data: A) extends Tree[A]

case object Empty extends Tree[Nothing]

object Tree {

  def empty: Empty.type = Empty

  def of[A](data: A): Tree[A] = Leaf(data)

  def insert[A](tree: Tree[A], data: A)(implicit ordering: Ordering[A]): Tree[A] = tree match {

    case Empty    =>
      Leaf(data)

    case Leaf(a)  =>
      if (ordering.compare(data, a) < 0)
        Node(a, Leaf(data), Empty)
      else
        Node(a, Empty, Leaf(data))

    case Node(a, l, r) =>
      if (ordering.compare(data, a) < 0)
        Node(a, Tree.insert(l, data), r)
      else
        Node(a, l, Tree.insert(r, data))
  }

  def insert[A](tree: Tree[A], l: List[A])(implicit ordering: Ordering[A]): Tree[A] =
    l.foldLeft(tree)((t, d) => insert(t, d))

  def inOrder[A](tree: Tree[A]): List[A] = tree match {
    case Empty         => List.empty[A]
    case Leaf(a)       => List(a)
    case Node(a, l, r) => inOrder(l) ++ List(a) ++ inOrder(r)
  }

}


object E008TreeApp {

  case class Money(value: Double)

  implicit val moneyOrdering: Ordering[Money] = new Ordering[Money] {
    def compare(x: Money, y: Money): Int = java.lang.Double.compare(x.value, y.value)
  }



  def main(args: Array[String]): Unit = {

    val t01 = Tree.insert(Tree.empty, List(5,6,3,0,9,1,13,5,-1))

    val t02 = Tree.insert(Tree.empty, Stream.continually(Random.nextDouble).take(5).map(Money).toList)

    println(Tree.inOrder(t01))

    println(Tree.inOrder(t02))
  }

}


object E008ImplicitTreeApp {

  case class Money(value: Double)

  implicit val moneyOrdering: Ordering[Money] = (x: Money, y: Money) => java.lang.Double.compare(x.value, y.value)

  implicit class TreeOps[A](tree: Tree[A]) {

    def insert(l: List[A])(implicit ordering: Ordering[A]): Tree[A] = Tree.insert(tree, l)

    def inOrder: List[A] = Tree.inOrder(tree)

  }



  def main(args: Array[String]): Unit = {

    val t01 = Tree.empty.insert(List(5,6,3,0,9,1,13,5,-1))

    val t02 = Tree.empty.insert(Stream.continually(Random.nextDouble).take(5).map(Money).toList)

    println(t01.inOrder)

    println(t02.inOrder)

  }

}
