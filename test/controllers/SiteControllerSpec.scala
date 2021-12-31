package controllers

import models.{Site, SiteUrl}
import persistence.SiteDao
import services.SiteService

import akka.actor.ActorSystem
import akka.stream.Materializer
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{DefaultActionBuilder, EssentialAction, Request}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class SiteControllerSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {
  "add site with good URL" in {
    val app: Application  =
      new GuiceApplicationBuilder()
        .build()
    val siteService = mock[SiteService]
    val siteDao = mock[SiteDao]
    val mockSite = new Site(1, "url", Option("iconUrl"), Option("title"))
    when(siteService.addSite(any[String])).thenReturn(Future.successful(mockSite))
    when(siteDao.add(any[String], any[Option[String]], any[Option[String]])).thenReturn(Future.successful(mockSite))

    val controller = app.injector.instanceOf[SiteController]
    val siteUrl = SiteUrl("https://www.cnn.com")
    val request = mock[Request[JsValue]]
    when(request.body) thenReturn Json.toJson(siteUrl)

    val response = await(controller.addSite()(request))
    response.header.status mustBe OK
    response.body.contentType mustBe Some("application/json")
    (contentAsJson(Future.successful(response)) \ "message").get.as[String] must include("java.lang.IllegalArgumentException")
  }

  "add site with bad URL" in {
    val app: Application  =
      new GuiceApplicationBuilder()
        .build()

    val controller = app.injector.instanceOf[SiteController]
    val siteUrl = SiteUrl("Name")
    val request = mock[Request[JsValue]]
    when(request.body) thenReturn Json.toJson(siteUrl)

    val response = await(controller.addSite()(request))
    response.header.status mustBe INTERNAL_SERVER_ERROR
    response.body.contentType mustBe Some("application/json")
    (contentAsJson(Future.successful(response)) \ "message").get.as[String] must include("java.lang.IllegalArgumentException")
  }
}
