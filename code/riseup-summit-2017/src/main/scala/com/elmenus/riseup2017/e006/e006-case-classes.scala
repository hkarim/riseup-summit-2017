package com.elmenus.riseup2017.e006

import java.time.LocalDate



object E006App01 {

  case class Name(value: String)
  case class Person(name: Name, birthDate: LocalDate)

  def main(args: Array[String]): Unit = {

    val p01 = Person(Name("p01"), LocalDate.of(1980, 11, 12))
    val p02 = Person(Name("p02"), LocalDate.of(1980, 11, 12))

    println( p01 )

    println( p01.copy(name = Name("p02")) )

    println( p01.copy(name = Name("p02")) == p02 )

    println( p01.productIterator.mkString(", ") )


  }

}




object E006App02 {

  case class Name(value: String)

  trait LivingBeing {
    def name: Name
  }

  case class Person(name: Name, birthData: LocalDate) extends LivingBeing
  case class Animal(name: Name) extends LivingBeing

  def classify(being: LivingBeing): String = being match {
    case Person(n, bd) => s"a person called: ${n.value} born ${bd.toString}"
    case Animal(n)     => s"an animal called: ${n.value}"
    case _             => s"a living being called: ${being.name.value}"
  }

  def main(args: Array[String]): Unit = {

    val person        = Person(Name("p01"), LocalDate.of(1980, 11, 12))
    val animal        = Animal(Name("a01"))
    val somethingElse = new LivingBeing {
      def name: Name = Name("unknown")
    }

    println( classify(person) )
    println( classify(animal) )
    println( classify(somethingElse) )


  }

}


