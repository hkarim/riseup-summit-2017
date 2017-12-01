package com.elmenus.riseup2017.e010

object FlatMapExample {

  def main(args: Array[String]): Unit = {

    val option      = Some(1).flatMap(i => Some(i + 1))
    val emptyOption = Option.empty[Int].flatMap(i => Some(i + 1))

    println(option)
    println(emptyOption)

    val list      = List(1,2,3).flatMap(i => List(s"number $i", s"número $i", s"nombre $i"))
    val emptyList = List.empty[Int].flatMap(i => List(s"number $i", s"número $i", s"nombre $i"))

    println(list)
    println(emptyList)
  }
}

object FlatMapModelExample {

  case class User(id: Long)
  case class UserProfile(id: Long)
  case class TwitterAccount(id: Long)

  trait UserService {

    def findUser(id: Long): Option[User]

    def findUserProfile(user: User): Option[UserProfile]

    def findTwitterAccount(userProfile: UserProfile): Option[TwitterAccount]

  }

  def userService: UserService = ???
}


object FlatMapAttempt01 {

  import FlatMapModelExample._

  def main(args: Array[String]): Unit = {

    val id: Long = 1L

    val twitterAccount: Option[TwitterAccount] =
      userService
        .findUser(id)
        .flatMap(user => userService.findUserProfile(user))
        .flatMap(userProfile => userService.findTwitterAccount(userProfile))

    println(twitterAccount)

  }
}


object FlatMapAttempt02 {

  import FlatMapModelExample._

  def main(args: Array[String]): Unit = {

    val id: Long = 1L

    val twitterAccount: Option[TwitterAccount] = for {
      user    <- userService.findUser(id)
      profile <- userService.findUserProfile(user)
      account <- userService.findTwitterAccount(profile)
    } yield account


    println(twitterAccount)

  }
}