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