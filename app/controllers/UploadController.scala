package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import java.sql.{Connection, DriverManager, ResultSet};
import java.util.Map;
import play.api.data._
import play.api.data.Forms._

@Singleton
class UploadController @Inject()(db: Database, cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
extends AbstractController(cc) with play.api.i18n.I18nSupport {
  
  def get() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.uploader())
  }
}