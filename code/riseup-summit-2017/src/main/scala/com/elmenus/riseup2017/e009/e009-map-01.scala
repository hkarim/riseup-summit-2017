package com.elmenus.riseup2017.e009


object MapAttempt01 {

  trait Container[A] {
    def map[B](f: A => B): Container[B]
  }

  trait Bag[A] {
    def map[B](f: A => B): Bag[B]
  }

  def change[A, B](instance: Container[A])(f: A => B): Container[B] = instance.map(f)
  def change[A, B](instance: Bag[A])(f: A => B): Bag[B] = instance.map(f)

  def container: Container[Int] = ???
  def bag: Bag[String] = ???

  change(container)(_ + 1)
  change(bag)(string => s"here is your $string")

}

object MapAttempt02 {

  trait Mapped[A] {
    def map[B](f: A => B): Mapped[B]
  }

  trait Container[A] extends Mapped[A]

  trait Bag[A] extends Mapped[A]


  def change[A, B](instance: Mapped[A])(f: A => B): Mapped[B] = instance.map(f)

  def container: Container[Int] = ???
  def bag: Bag[String] = ???

  change(container)(_ + 1)
  change(bag)(string => s"here is your $string")

}


object MapAttempt03 {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  trait Container[A]

  trait Bag[A]


  def change[F[_], A, B](instance: F[A])(f: A => B)(functor: Functor[F]): F[B] = functor.map(instance)(f)

  def containerFunctor: Functor[Container] = ???
  def container: Container[Int] = ???

  def bagFunctor: Functor[Bag] = ???
  def bag: Bag[String] = ???

  change(container)(_ + 1)(containerFunctor)
  change(bag)(string => s"here is your $string")(bagFunctor)

}

object MapAttempt04 {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  trait Container[A]

  trait Bag[A]


  def change[F[_], A, B](instance: F[A])(f: A => B)(implicit functor: Functor[F]): F[B] = functor.map(instance)(f)

  implicit def containerFunctor: Functor[Container] = ???
  def container: Container[Int] = ???

  implicit def bagFunctor: Functor[Bag] = ???
  def bag: Bag[String] = ???

  change(container)(_ + 1)
  change(bag)(string => s"here is your $string")

}



