package services

import models.Site
import persistence.SiteDao

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SiteServiceSpec extends PlaySpec with MockitoSugar {

  "add" should {
    "be true when the role is admin" in {
      val mockSite = new Site(1, "url", Option("iconUrl"), Option("title"))
      val siteDao = mock[SiteDao]
      when(siteDao.add(any[String], any[Option[String]], any[Option[String]])).thenReturn(Future.successful(mockSite))

      val siteService = new SiteService(siteDao)

      val actual = siteService.addSite("url")
      actual mustBe true
    }
  }
}
