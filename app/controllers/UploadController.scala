package controllers

import models._

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.db.slick._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.streams._
import play.api.mvc._
import play.api.mvc.MultipartFormData.FilePart
import play.core.parsers.Multipart.FileInfo
import _root_.slick.jdbc.JdbcProfile

import java.io.File
import java.nio.file.{Files, Path}
import javax.inject._

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.util.{Try, Success, Failure}
import scala.concurrent.{ExecutionContext, Future}
import java.nio.file.Paths
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class FormData(name: String)

@Singleton
class UploadController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents, config: Configuration)(
    implicit ec: ExecutionContext, assetsFinder: AssetsFinder) extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] with play.api.i18n.I18nSupport{
  
  import dbConfig.profile.api._
  val path = config.get[String]("file_path")

  def renderUser = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.uploader(User.form))
  }

  def renderUpload(id: Int) = Action { implicit request =>
    Ok(views.html.fileuploader(UploadForm.form, id))
  }

  def renderMessage(id: Int, fId: Int) = Action { implicit request =>
    Ok(views.html.message(Message.form, id, fId))
  }

  def done(id: Int) = Action { implicit request =>
    Ok(views.html.done(id))
  }


  def submit = Action.async { implicit request => 
    User.form.bindFromRequest().fold(
      formWithErrors => Future{
          BadRequest(views.html.uploader(formWithErrors))
        },
        user => {
          val users = TableQuery[Tables.User]

          val maybeId : Future[Option[Int]] = db.run(users
                  .filter(_.email === user.email)
                  .map(_.id).result)
                .map(_.headOption)

          maybeId.flatMap{ 
            case Some(id) => Future{Redirect(routes.UploadController.renderUpload(id))}
            case None => {
              db.run((users returning users.map(_.id)) += Tables.UserRow(0, user.name, user.email)).map(id =>
                Redirect(routes.UploadController.renderUpload(id)))
            }
          }
      }
    )
  }

  def upload(id: Int) = Action(parse.multipartFormData) { request =>
    request.body
      .file("File")
      .flatMap{ file =>
        // first try to get the file
        val filename = Paths.get(file.filename).getFileName

        //save filename and user to db
        val files = TableQuery[Tables.UserFile]

        file.ref.copyTo(Paths.get(s"$path/$filename"), replace = false)
        val fid : Future[Int] = db.run((files returning files.map(_.id)) += Tables.UserFileRow(0, id, filename.toString))
          
        Await.ready(fid, Duration(20, "seconds")) 
        
        fid.value.map{
          case Success(fileId) => Redirect(routes.UploadController.renderMessage(id, fileId))
          case Failure(e) => Redirect(routes.UploadController.renderUpload(id))
        }
      }.getOrElse(Redirect(routes.UploadController.renderUpload(id)))
    }

  def message(id: Int, fId: Int) = Action.async { implicit request => 
    Message.form.bindFromRequest().fold(
      formWithErrors => Future{
          BadRequest(views.html.message(formWithErrors, id, fId))
        },
        m => {
          val messages = TableQuery[Tables.Message]
          val privacy = if(m.isPrivate=="private") true else false

          db.run(messages += Tables.MessageRow(id, fId, Some(m.message), privacy))
              .map(_ => Redirect(routes.UploadController.done(id)))        
        }
    )
  }

}