package models

import play.api.libs.json.{Format, Json}

case class Site(id: Int, url: String, iconUrl: Option[String], title: Option[String])

case class SiteUrl(url: String)

object Site {
  implicit val siteFormats: Format[Site] = Json.format[Site]
}

object SiteUrl {
  implicit val siteUrlFormats: Format[SiteUrl] = Json.format[SiteUrl]
}
