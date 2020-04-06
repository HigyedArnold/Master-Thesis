package com.planr.rest

import com.google.inject.AbstractModule
import com.planr.api.async.MainService
import com.planr.rest.impl.MainServiceImpl
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[MainService].to[MainServiceImpl].asEagerSingleton()
    ()
  }
}
