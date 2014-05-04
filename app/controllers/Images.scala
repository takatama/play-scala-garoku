package controllers

import play.api._
import play.api.mvc._

import views._
import models.Image
import Security._

object Images extends Controller with Secured {

  def prepareUpload = SecureAction { user => implicit request =>
    Ok(views.html.upload())
  }

  def upload = Action(parse.multipartFormData) { request => 
      request.body.file("image").map { image =>
        import java.io.File
        val filename = image.filename
        val contentType = image.contentType
        image.ref.moveTo(new File(s"/tmp/image/$filename"), true)
        Ok("File uploaded")
      }.getOrElse {
        Redirect(routes.Images.prepareUpload).flashing(
          "error" -> "Missing file"
        )
      }
  }

  def show(id: Long) = SecureAction { user => _ =>
    Image.find(id) match {
      case None => BadRequest("No such image.")
      case Some(image) => Ok.sendFile(new java.io.File(image.path))
    }
  }
}
