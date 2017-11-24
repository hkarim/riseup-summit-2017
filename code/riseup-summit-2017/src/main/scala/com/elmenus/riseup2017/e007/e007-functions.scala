package com.elmenus.riseup2017.e007


object Fn {

  def combineAll[A](first: A, rest: A*)(combine: (A, A) => A): A =
    rest.foldLeft(first)(combine)

}

object E007FunctionalApp {

  import Fn._


  def main(args: Array[String]): Unit = {

    println(
      combineAll(1, 2, 3, 4)( (l, r) => l + r )
    )

    println(
      combineAll(1, 2, 3, 4)( _ + _ )
    )

    println(
      combineAll("1", "2", "3", "4") { (l, r) =>
        s"$l and $r"
      }
    )

  }


}