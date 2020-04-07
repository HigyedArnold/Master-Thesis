package com.planr.rest

import com.google.inject.AbstractModule
import com.planr.api.async.TestService
import com.planr.rest.impl.TestServiceImpl
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[TestService].to[TestServiceImpl].asEagerSingleton()
    ()
  }
}
