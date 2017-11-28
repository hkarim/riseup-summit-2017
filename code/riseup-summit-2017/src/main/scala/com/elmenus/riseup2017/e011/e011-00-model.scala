package com.elmenus.riseup2017.e011

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import slick.jdbc.GetResult

import scala.concurrent.ExecutionContext


case class City(
  id: Long,
  name: String,
  countryCode: String,
  district: String,
  population: Long)

case class Country(
  id: Long,
  name: String,
  continent: String,
  region: String,
  surfaceArea: Double,
  independenceYear: Int,
  population: Long,
  lifeExpectancy: Double,
  gnp: Double,
  localName: String,
  governmentForm: String,
  headOfState: String,
  capital: Long)

object SlickFormat {

  implicit val grCity: GetResult[City] = GetResult[City] { r =>
    City(
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<
    )
  }

  implicit val grCountry: GetResult[Country] = GetResult[Country] { r =>
    Country(
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<,
      r.<<
    )
  }
}


trait Context {
  implicit val actorSystem: ActorSystem             = ActorSystem()
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext   = actorSystem.dispatcher

  lazy val configuration: Config = actorSystem.settings.config
}