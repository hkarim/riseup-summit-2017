package com.elmenus.riseup2017.e009



object Options {

  def main(args: Array[String]): Unit = {

    val o1: Option[Int] = Some(1)
    val o2: Option[Int] = None

    def run[A, B](instance: Option[A])(f: A => B): Unit = println(instance.map(f))

    run(o1)( _ + 1 )
    run(o2)( _ + 1 )

  }

}

object Collections {

  def main(args: Array[String]): Unit = {

    val v1: Vector[Int] = Vector(1,2,3)
    val v2: Vector[Int] = Vector.empty[Int]

    def run[A, B](instance: Vector[A])(f: A => B): Unit = println(instance.map(f))

    run(v1)( _ + 1 )
    run(v2)( _ + 1 )
  }

}

object Futures {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {

    val f1: Future[Int] = Future.successful(1)
    val f2: Future[Int] = Future.failed[Int](new NoSuchElementException)

    def run[A, B](instance: Future[A])(f: A => B): Unit = println(instance.map(f))

    run(f1)( _ + 1 )
    run(f2)( _ + 1 )
  }

}

object Functors {

  import cats._
  import cats.implicits._
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  //def run[F[_], A, B](instance: F[A])(f: A => B)(implicit ev: Functor[F]): Unit = println(instance.map(f))
  def run[F[_]: Functor, A, B](instance: F[A])(f: A => B): Unit = println(instance.map(f))

  def main(args: Array[String]): Unit = {

    val o1: Option[Int] = Some(1)
    val o2: Option[Int] = None

    run(o1)( _ + 1 )
    run(o2)( _ + 1 )

    val v1: Vector[Int] = Vector(1,2,3)
    val v2: Vector[Int] = Vector.empty[Int]

    run(v1)( _ + 1 )
    run(v2)( _ + 1 )

    val f1: Future[Int] = Future.successful(1)
    val f2: Future[Int] = Future.failed[Int](new NoSuchElementException)

    Monad[Future].pure(1)

    run(f1)( _ + 1 )
    run(f2)( _ + 1 )

  }




}