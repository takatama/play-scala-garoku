package controllers

import play.api._
import play.api.mvc._

import views._

object Application extends Controller with Secured {
  def index = SecureAction { user => _ =>
    Redirect(routes.Images.list)
  }
}
