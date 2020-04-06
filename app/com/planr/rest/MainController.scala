package com.planr.rest

import com.google.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, InjectedController}

@Singleton
class MainController @Inject() () extends InjectedController {

  def index: Action[AnyContent] = Action {
    Ok("RST server started...")
  }

}
