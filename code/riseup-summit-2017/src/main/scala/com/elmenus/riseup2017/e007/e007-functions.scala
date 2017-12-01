package com.elmenus.riseup2017.e007


object FoldLeftApp {

  def main(args: Array[String]): Unit = {
    val schedule = List("had a nice breakfast", "went to RiseUp", "met some brilliant people")

    val folded = schedule.foldLeft("woke up early")( (accumulator, next) => s"$accumulator then $next")

    println(folded)
  }
}


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

object E007ApplyFunction {

  // Company trait
  trait Company {
    def name: String
  }

  // Company companion object
  object Company {

    def apply(companyName: String): Company = new Company {
      def name: String = companyName
    }

  }

  def main(args: Array[String]): Unit = {

    val c01 = Company.apply("c01") // invoke `apply` explicitly
    val c02 = Company("c02")       // invoke `apply`, syntactic sugar

    println(c01)
    println(c02)

  }
}