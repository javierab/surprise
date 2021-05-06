package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.db.slick._
import java.sql.{Connection, DriverManager, ResultSet};
import java.util.Map;
import play.api.data._
import play.api.data.Forms._
import scala.util.{Try, Success, Failure}
import models._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import _root_.slick.jdbc.JdbcProfile

@Singleton
class UploadController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(
    implicit ec: ExecutionContext, assetsFinder: AssetsFinder) extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] with play.api.i18n.I18nSupport{
  
  def get() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.uploader(User.form))
  }

  def submit() = Action.async { implicit request => 
    User.form.bindFromRequest().fold(
      formWithErrors => Future{
          BadRequest(views.html.uploader(formWithErrors))
        },
        user => {
          import dbConfig.profile.api._
          val users = TableQuery[Tables.User]

          db.run((users returning users.map(_.id)) += Tables.UserRow(0, user.name, user.email))
            .map(id => Redirect(routes.UploadController.uploadFile(id.toString)))
      }
    )
  }

  def uploadFile(id: String) = Action { implicit request =>
    Ok(views.html.fileuploader(id))
  }

  }