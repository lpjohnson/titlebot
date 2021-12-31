package controllers

import models.SiteUrl

import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Request
import play.api.test.Helpers._

import scala.concurrent.Future

class SiteControllerSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  "catch exception and return 500 when bad url is passed in" in {
    val app: Application =
      new GuiceApplicationBuilder()
        .build()

    val controller = app.injector.instanceOf[SiteController]
    val siteUrl = SiteUrl("Name")
    val request = mock[Request[JsValue]]
    when(request.body) thenReturn Json.toJson(siteUrl)

    val response = await(controller.addSite()(request))
    response.header.status mustBe INTERNAL_SERVER_ERROR
    response.body.contentType mustBe Some("application/json")
    (contentAsJson(Future.successful(response)) \ "message").get.as[String] must include(
      "java.lang.IllegalArgumentException"
    )
  }
}
