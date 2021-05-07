package models

import play.api.data.Form
import play.api.data.Forms._
import views.html.helper.textarea

case class User(name: String, email: String) 
object User {

  val form: Form[User] = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Email" -> email,
    )(User.apply)(User.unapply)
      )
}

case class UploadForm(file: String)
object UploadForm {

  val form: Form[UploadForm] = Form(
    mapping(
      "File" -> nonEmptyText
    )(UploadForm.apply)(UploadForm.unapply)
  )
}

case class Message(message: Option[String], isPrivate: String) 
object Message {

  val form: Form[Message] = Form(
    mapping(
      "Your message" -> optional(text),
      "Privacy" -> nonEmptyText
    )(Message.apply)(Message.unapply)
  )
}