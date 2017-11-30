package com.elmenus.riseup2017.e011

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl.{Flow, Source}
import io.circe.generic.auto._
import io.circe.syntax._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import scala.io.StdIn

object WebServer extends Context {

  val ip: String = configuration.getString("http.bind.ip")
  val port: Int = configuration.getInt("http.bind.port")

  import SlickFormat._

  val postgres: Database = Database.forConfig("db.world", configuration)

  // Data source, populated from DB
  val city: Source[City, Any] =
    Source
      .fromPublisher(
        postgres.stream(
          sql"select id, name, countrycode, district, population from city"
            .as[City]))

  // Map City into Text Message
  def map(city: City): Message =
    TextMessage(city.asJson.noSpaces)

  // Construct WS source
  // def not a val so that a new source is created with each WS open
  def source: Source[Message, Any] =
    city
      .map(map)
      .delay(1.second, DelayOverflowStrategy.backpressure)
      .withAttributes(Attributes.inputBuffer(initial = 10, max = 100))

  // Construct WS flow
  val cityWebSocketService: Flow[Message, Message, Any] =
    Flow[Message]
      .flatMapConcat(_ => source)

  def main(args: Array[String]): Unit = {
    val route =
      pathPrefix("city") {
        pathEndOrSingleSlash {
          handleWebSocketMessages(cityWebSocketService)
        }
      }

    val bindingFuture = Http().bindAndHandle(route, ip, port)

    println(s"Server online at http://$ip:$port/\nPress RETURN to stop...")
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        postgres.close()
        actorSystem.terminate()
      })
  }
}
