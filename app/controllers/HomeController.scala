package controllers

import models.{UserProfile, UserProfileData}

import javax.inject._
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import service.UserProfileService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents , userProfileService: UserProfileService) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

    implicit val userProfileJson = Json.format[UserProfile]
    implicit val userProfileData = Json.format[UserProfileData]
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getAll() = Action.async{ implicit request : Request[AnyContent] =>
    userProfileService.listAllUser.map {
      items => Ok(Json.toJson(items))
    }
  }

  def getById(id:Long) = Action.async { implicit request: Request[AnyContent] =>
    userProfileService.getUser(id).map{
      item => Ok(Json.toJson(item))
    }
  }

  def delete(id:Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    userProfileService.deleteUser(id).map{ res =>
     Redirect(routes.HomeController.getAll)
    }

  }

  def addUser(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[UserProfileData].asOpt
      .fold {
        Future.successful(BadRequest("No item added"))
      } {
        response =>
            val newItemAdded = UserProfile(0, response.firstName, response.lastName,response.email)
            userProfileService.addUser(newItemAdded).map { res =>
          Redirect(routes.HomeController.getAll)
            }
      }
  }

  def update(id: Long):Action[JsValue] = Action(parse.json) { implicit request =>
    request.body.validate[UserProfileData].asOpt
      .fold {
        BadRequest("No item updated")
      } {
        response =>
          val newItemAdded = UserProfile(0, response.firstName, response.lastName,response.email)
          userProfileService.update(newItemAdded)
           Redirect(routes.HomeController.getAll)
      }
  }


}
