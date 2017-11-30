package com.elmenus.riseup2017.e011

import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.GetResult
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object DB01 {

  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val configuration: Config = ConfigFactory.load()
  lazy val db: Database = Database.forConfig("db.elmenus", configuration)

  def main(args: Array[String]): Unit = {

    val io: DBIO[Vector[Int]] = sql"select 1".as[Int]

    val f: Future[Vector[Int]] = db.run(io)

    f.onComplete {
      case Success(v) => println(v)
      case Failure(e) => e.printStackTrace()
    }

    Await.result(f, Duration.Inf)

    db.close()
  }

}


object DB02 {

  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val configuration: Config = ConfigFactory.load()
  lazy val db: Database = Database.forConfig("db.elmenus", configuration)
  lazy val log: Logger = LoggerFactory.getLogger("DB02")

  case class Account(id: Long, email: String)

  implicit val grUser: GetResult[Account] = GetResult { r =>
    Account(r.<<, r.<<)
  }

  val createAccountTable: DBIO[Int] =  sqlu"create table if not exists account(id bigint, email varchar(255))"
  def insertAccount(account: Account): DBIO[Int] = sqlu"insert into account values (${account.id}, ${account.email})"
  val findAllAccounts: DBIO[Vector[Account]] = sql"select id, email from account".as[Account]
  val dropAccountTable: DBIO[Int] = sqlu"drop table account"


  def up(account: Account): DBIO[Vector[Account]] = for {
    _        <- createAccountTable
    _        <- insertAccount(account)
    accounts <- findAllAccounts
    _        <- dropAccountTable
  } yield accounts

  def main(args: Array[String]): Unit = {

    val u01: Account = Account(1L, "one@acm.org")

    val f = db.run(up(u01).transactionally)

    f.onComplete {
      case Success(v) => log.info(v.mkString(","))
      case Failure(e) => log.error("failed", e)
    }

    Await.result(f, Duration.Inf)

    db.close()

  }


}


object DB03 extends Context {

  import SlickFormat._

  import scala.concurrent.duration._

  lazy val db: Database = Database.forConfig("db.world", configuration)
  lazy val log: Logger = LoggerFactory.getLogger("DB03")

  val findAllAccounts: StreamingDBIO[Vector[City], City] =
    sql"select id, name, countrycode, district, population from city".as[City]


  def main(args: Array[String]): Unit = {

    val f =
      Source
      .fromPublisher(db.stream(findAllAccounts.transactionally))
      .delay(1.second, OverflowStrategy.backpressure)
      .runForeach(println)

    f.onComplete(_ => actorSystem.terminate())

    Await.result(f, Duration.Inf)

    db.close()

  }


}