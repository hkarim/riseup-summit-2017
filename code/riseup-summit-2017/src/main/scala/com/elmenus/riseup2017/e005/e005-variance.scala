package com.elmenus.riseup2017.e005


object Variance {

  class Invariant[A]

  class Covariant[+A]

  class Contravariant[-A]

  trait LivingBeing
  trait Animal extends LivingBeing
  class Cat extends Animal

}

object E005VarianceApp {

  import Variance._

  def invariant(instance: Invariant[Animal]): Unit         = println(instance)  // Animal only
  def covariant(instance: Covariant[Animal]): Unit         = println(instance)  // Animal or some type that    **extends**    Animal
  def contravariant(instance: Contravariant[Animal]): Unit = println(instance)  // Animal or some type that is **super**   to Animal

  def main(args: Array[String]): Unit = {

    //invariant(new Covariant[LivingBeing])                            // will not compile
    invariant(new Invariant[Animal])
    //invariant(new Covariant[Cat])                                    // will not compile




    //covariant(new Covariant[LivingBeing])                            // will not compile
    covariant(new Covariant[Animal])
    covariant(new Covariant[Cat])




    contravariant(new Contravariant[LivingBeing])
    contravariant(new Contravariant[Animal])
    //contravariant(new Contravariant[Cat])                             // will not compile
  }


}
