package persistence

import models.Site

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class SiteDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val sitesTable = TableQuery[SitesTable]

  def fetchAll(): Future[Seq[Site]] = db.run(sitesTable.result)

  def add(url: String, iconUrl: Option[String], title: Option[String]): Future[Site] = {
    // When you include an AutoInc column in an insert operation, it is silently ignored
    // as the database handle incrementing
    val newSite = Site(0, url, iconUrl, title)
    val query = for {
      id <- (sitesTable returning sitesTable.map(_.id)) += newSite
      result <- sitesTable.filter(_.id === id).result.head
    } yield result
    db.run(query.transactionally)
  }

  // Constructor for Slick MySQL database configuration
  private class SitesTable(tag: Tag) extends Table[Site](tag, "sites") {
    // Automatically increments the index by 1
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def url = column[String]("url")
    def iconUrl = column[Option[String]]("iconUrl")
    def title = column[Option[String]]("title")

    override def * =
      (id, url, iconUrl, title) <> ((Site.apply _).tupled, Site.unapply)
  }
}
