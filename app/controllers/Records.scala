package controllers

import play.api._
import play.api.mvc._

import views._

object Records extends Controller with Secured {
  def index = SecureAction { user => _ =>
    Ok(views.html.index("Your new application is ready."))
  }

  def prepareUpload = TODO
  def upload = TODO

  def list = TODO
  def show(id: Long) = TODO
}
