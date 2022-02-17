package service

import db.UserProfileList
import models.UserProfile

import javax.inject.Inject
import scala.concurrent.Future

class UserProfileService @Inject()(items:UserProfileList) {

  def deleteUser(id:Long)={
    items.delete(id)
  }

  def update(user:UserProfile): Future[Int] ={
    items.update(user)
  }
  def getUser(id: Long): Future[Option[UserProfile]] = {
    items.get(id)
  }

  def listAllUser: Future[Seq[UserProfile]] = {
    items.listAll
  }
}
