package com.elmenus.riseup2017.e004

import java.time.LocalDate

class Person(val name: String, val birthDate: LocalDate) // name and birthDate are immutable

object E004App {

  def show(person: Person): String =
    s"The person's name is ${person.name} and his/her birth date is ${person.birthDate.toString}"

  lazy val localDateOnFirstCall: LocalDate = LocalDate.now()

  def main(args: Array[String]): Unit = {

    println(s"App started on ${localDateOnFirstCall.toString}")

    val p01: Person = new Person("p01", LocalDate.of(1970, 4, 15)) // explicit type declaration
    val p02         = new Person("p02", LocalDate.of(1988, 10, 4)) // type inference

    println(show(p01))
    println(show(p02))

  }
}