package controllers

import models.SiteUrl
import services.SiteService

import com.google.inject.Inject
import play.api.libs.json.{JsError, Json}
import play.api.mvc._

import javax.inject.Singleton
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SiteController @Inject()(siteService: SiteService)
  (cc: ControllerComponents)
  (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index() = Action { implicit request =>
    Ok(views.html.index())
  }

  def addSite() = Action.async(parse.json) { implicit request =>
    request.body.validate[SiteUrl].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      model => {
        (for {
          newSite <- siteService.addSite(model.url)
        } yield {
          Ok(Json.toJson(newSite))
        }).recover {
          case e =>
            InternalServerError(Json.obj("message" ->  s"internal server error; $e"))
        }
      }
    )
  }

  def listSites() = Action.async { implicit request =>
    for {
      sites <- siteService.listAllSites
    } yield {
      Ok(Json.toJson(sites))
    }
  }
}
