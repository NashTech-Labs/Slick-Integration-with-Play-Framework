package db

import models.UserProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Rep, TableQuery, Tag}

import java.awt.image.LookupTable
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserProfileServiceImpl(tag:Tag) extends Table[UserProfile](tag,"user"){
def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
  def firstName = column[String]("name")
  def lastName = column[String]("firstname")
  def email = column[String]("email")

  override def * : ProvenShape[UserProfile] = (id,firstName,lastName,email)<>(UserProfile.tupled,UserProfile.unapply)
}

class UserProfileList @Inject()(protected val dbConfigProvider:DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  var users = TableQuery[UserProfileServiceImpl]


  def delete(id:Long) ={
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def update(user:UserProfile) ={
    dbConfig.db.run(users.filter(_.id === user.id)
    .map(x => (x.firstName , x.lastName , x.email))
    .update(user.firstName,user.lastName,user.email))
  }

  def get(id:Long)={
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll ={
    dbConfig.db.run(users.result)
  }
}
