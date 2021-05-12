package controllers

import models._
import views._
import javax.inject._
import play.api._

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.db.slick._
import _root_.slick.jdbc.JdbcProfile

import scala.util.{Try, Success, Failure}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DisplayController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents, config: Configuration)(
    implicit ec: ExecutionContext, assetsFinder: AssetsFinder) extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] with play.api.i18n.I18nSupport{
  
  import dbConfig.profile.api._
  val path = config.get[String]("file_path")

    
  def displayImages = Action.async { implicit request =>

    val users = TableQuery[Tables.User]
    val files = TableQuery[Tables.UserFile]
    val messages = TableQuery[Tables.Message]


    //get names + images to show.
    val cQuery = for {
      f <- files
      u <- users if f.userId === u.id
      m <- messages if f.id === m.fid
    } yield (u.name, f.filename, m.message)

    db.run(cQuery.result)
      .map(ccFut => ccFut
                      .filter(f => f._2.endsWith("png") || f._2.endsWith("jpg") || f._2.endsWith("jpeg"))
                      .map(c => Card(c._1, s"$path/${c._2}", c._3)))
      .map(futCards => Ok(views.html.cards(futCards)))
    
    
  }

}