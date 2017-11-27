package com.elmenus.riseup2017.e011

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.Source
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.GetResult
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object DB01 {

  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val configuration: Config = ConfigFactory.load()
  lazy val db: Database = Database.forConfig("db.elmenus", configuration)

  def main(args: Array[String]): Unit = {

    val f = db.run(sql"select 1".as[Int])

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


object DB03 {

  import scala.concurrent.duration._

  implicit val actorSystem: ActorSystem             = ActorSystem()
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext   = actorSystem.dispatcher

  lazy val configuration: Config = actorSystem.settings.config
  lazy val db: Database = Database.forConfig("db.world", configuration)
  lazy val log: Logger = LoggerFactory.getLogger("DB03")

  case class City(id: Long, name: String, countryCode: String, district: String, population: Long)

  implicit val grUser: GetResult[City] =
    GetResult(r => City(r.<<, r.<<, r.<<, r.<<, r.<<))

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