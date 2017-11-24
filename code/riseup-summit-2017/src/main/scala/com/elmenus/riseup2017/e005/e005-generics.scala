package com.elmenus.riseup2017.e005

import scala.annotation.tailrec


object Generics {

  trait Combiner[A] {
    def combine(left: A, right: A): A
  }

  object StringCombiner extends Combiner[String] {
    def combine(left: String, right: String): String = s"$left and $right"
  }

  object IntegerCombiner extends Combiner[Int] {
    def combine(left: Int, right: Int): Int = left + right
  }

}


object E005GenericsApp {

  import Generics._

  @tailrec def combineAll[A](first: A, rest: A*)(combiner: Combiner[A]): A =
    if (rest.isEmpty)
      first
    else
      combineAll(
        combiner.combine(first, rest.head),
        rest.tail:_*
      )(combiner)


  def main(args: Array[String]): Unit = {

    println(combineAll(1, 2, 3, 4)(IntegerCombiner))

    println(combineAll("1", "2", "3", "4")(StringCombiner))

  }


}
