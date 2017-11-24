package com.elmenus.riseup2017.e003

trait DatabaseAccess

trait Networking

trait Service {
  self: DatabaseAccess with Networking =>
}


trait PostgresDatabaseAccess extends DatabaseAccess

trait TcpNetworking extends Networking


object MyService extends Service with PostgresDatabaseAccess with TcpNetworking