package com.elmenus.riseup2017.e011

import akka.stream.KillSwitches
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.{Done, NotUsed}
import com.mongodb.reactivestreams.client._
import io.circe.generic.auto._
import io.circe.syntax._
import org.bson.Document
import slick.jdbc.PostgresProfile.api._

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.duration._

object Mongo01 extends Context {

  import SlickFormat._

  val client: MongoClient = MongoClients.create()

  val mongo: MongoDatabase = client.getDatabase("world")

  val postgres: Database = Database.forConfig("db.world", configuration)

  val cityCollection: MongoCollection[Document] = mongo.getCollection("city")

  val citySource: Source[City, NotUsed] =
    Source
      .fromPublisher(
        postgres.stream(sql"select id, name, countrycode, district, population from city limit 100".as[City]))

  val cityFlow: Flow[City, Document, NotUsed] =
    Flow[City]
      .map(_.asJson.noSpaces)
      .map(Document.parse)

  val citySink: Sink[Document, Future[Done]] =
    Sink.foreach[Document] { doc =>
      Source
        .fromPublisher(cityCollection.insertOne(doc))
        .recover {
          case e =>
            e.printStackTrace()
        }
        .runForeach(println)
      ()
    }



  def main(args: Array[String]): Unit = {

    val graph = citySource.via(cityFlow).viaMat(KillSwitches.single)(Keep.right).to(citySink)

    val killSwitch = graph.run()

    scala.io.StdIn.readLine()


    killSwitch.shutdown()
    actorSystem.terminate.onComplete {
       _ => client.close()
    }

  }


}


object Mongo02 extends Context {

  import SlickFormat._

  val client: MongoClient = MongoClients.create()

  val mongo: MongoDatabase = client.getDatabase("world")

  val postgres: Database = Database.forConfig("db.world", configuration)

  val cityCollection: MongoCollection[Document] = mongo.getCollection("city")

  val citySource: Source[City, NotUsed] =
    Source
      .fromPublisher(
        postgres.stream(sql"select id, name, countrycode, district, population from city limit 100".as[City]))

  val cityFlow: Flow[City, Vector[Document], NotUsed] =
    Flow[City]
      .map(_.asJson.noSpaces)
      .map(Document.parse)
      .groupedWithin(10, 3.seconds)
      .map(_.toVector)

  val citySink: Sink[Vector[Document], Future[Done]] =
    Sink.foreach[Vector[Document]] { docs =>
      Source
        .fromPublisher(cityCollection.insertMany(docs.asJava))
        .recover {
          case e =>
            e.printStackTrace()
        }
        .runForeach(println)
      ()
    }



  def main(args: Array[String]): Unit = {

    val graph = citySource.via(cityFlow).viaMat(KillSwitches.single)(Keep.right).to(citySink)

    val killSwitch = graph.run()

    scala.io.StdIn.readLine()


    killSwitch.shutdown()
    actorSystem.terminate.onComplete {
      _ => client.close()
    }

  }


}