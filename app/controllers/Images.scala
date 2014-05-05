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

  def upload = SecureAction(parse.multipartFormData) { user => implicit request => 
      request.body.file("image").map { image =>
        import java.io.File
        val contentType = image.contentType
	contentType match {
	  case None => Redirect(routes.Images.prepareUpload).flashing(
	    "error" -> "Missing Content-Type"
	  )
	  case Some(c) => {
            val uuid = java.util.UUID.randomUUID().toString();
	    val path = "/tmp/image/" + uuid + "_" + image.filename
            image.ref.moveTo(new File(path), true)
	    Image.create(c, path, user)
            Redirect(routes.Images.list).flashing(
	      "success" -> "File uploaded"
	    )
	  }
	}
      }.getOrElse {
        Redirect(routes.Images.prepareUpload).flashing(
          "error" -> "Missing file"
        )
      }
  }

  def show(id: Long) = SecureAction { user => _ =>
    Image.find(id) match {
      case None => BadRequest("No such image.")
      case Some(image) => {
        Image.addLog(image, user)
        Ok.sendFile(content = new java.io.File(image.path), inline = true).withHeaders(CONTENT_TYPE -> image.contentType)
      }
    }
  }

  def list = SecureAction { user => implicit request =>
    val images = Image.findByUser(user)
    Ok(views.html.list(images))
  }
}
