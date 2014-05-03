package controllers

import play.api._
import play.api.mvc._

import views._
import models.Image

object Images extends Controller with Secured {
  def show(id: Long) = SecureAction { user => _ =>
    Image.find(id) match {
      case None => BadRequest("No such image.")
      case Some(image) => Ok.sendFile(new java.io.File(image.path))
    }
  }
}
