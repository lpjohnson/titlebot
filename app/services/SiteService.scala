package services

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import com.google.inject.Inject
import models.Site
import persistence.SiteDao

import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.{ExecutionContext, Future}

class SiteService @Inject()(siteDao: SiteDao)(implicit executionContext: ExecutionContext) {

  /**
   * Adds new site
   * @param url: String
   * @return New site added to database
   */
  def addSite(url: String): Future[Site] = {
    val getUrl: Future[Document] = Future {JsoupBrowser().get(url) }
    for {
      contents: Document <- getUrl
      iconUrl <- Future.successful(getIconUrl(url, contents))
      title <- Future.successful(contents.title)
      newSite <- siteDao.add(url, iconUrl, Option(title))
    } yield newSite
  }

  /**
   * List all sites in database
   * @return Seq of all sites
   */
  def listAllSites: Future[Seq[Site]] = {
    siteDao.fetchAll()
  }

  /**
   * Gets and constructs URL for site favicon
   * @param url: String
   * @param contents: Document
   * @return A full URL pointing to the favicon for the provided site URL
   */
  private def getIconUrl(url: String, contents: Document): Option[String] = {
    // Find the first instance of the <link> element containing a href with a common file extension
    val maybeIconUrl = contents >?> element("link[href~=.*\\.png|.*\\.jpg|.*\\.ico]") >?> attr("href")
    val isNotFullUrl = maybeIconUrl flatMap(_.map(_.startsWith("/")))
    // If the icon url is a relative URL, strip the site URL of the trailing slash and tack on the relative URL
    isNotFullUrl match {
      case Some(true) =>
        val stripped = if (url.endsWith("/")) {
          url.dropRight(1)
        } else {
          url
        }
        maybeIconUrl.flatMap(_.map(i => stripped + i))
      case _ =>
        maybeIconUrl.flatten
    }
  }

}
