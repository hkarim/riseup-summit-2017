package com.elmenus.riseup2017.e001

trait Organization

class Company extends Organization

class University extends Organization

object IBM extends Company

object AUC extends University

object TypeAlias {

  type Name = String

  type Money = Double


}



