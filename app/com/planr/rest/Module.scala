package com.planr.rest

import com.google.inject.AbstractModule
import com.planr.api.async.{SolveServiceT, TestServiceT}
import com.planr.api.sync.InitServiceT
import com.planr.rest.impl.{InitService, SolveService, TestService}
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[InitServiceT].to[InitService].asEagerSingleton()
    bind[TestServiceT].to[TestService]
    bind[SolveServiceT].to[SolveService]
    ()
  }
}
