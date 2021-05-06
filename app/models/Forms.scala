package models

import play.api.data.Form
import play.api.data.Forms._

case class User(name: String, email: String) 
object User {

  val form: Form[User] = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email
    )(User.apply)(User.unapply)
      )
}

case class UploadImgForm(message: String, isPrivate: Boolean)

object UploadImgForm {

  val form: Form[UploadImgForm] = Form(
    mapping(
      "message" -> nonEmptyText,
      "isPrivate" -> boolean
    )(UploadImgForm.apply)(UploadImgForm.unapply)
      )
}

case class UploadVidForm(name: String)
object UploadVidForm {

  val form: Form[UploadVidForm] = Form(
    mapping(
      "name" -> nonEmptyText,
    )(UploadVidForm.apply)(UploadVidForm.unapply)
      )
}