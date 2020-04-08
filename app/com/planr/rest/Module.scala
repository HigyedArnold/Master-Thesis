package com.planr.rest

import com.google.inject.AbstractModule
import com.planr.api.async.{SolverServiceT, TestServiceT}
import com.planr.api.sync.InitServiceT
import com.planr.rest.impl.{InitService, SolverService, TestService}
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[InitServiceT].to[InitService].asEagerSingleton()
    bind[TestServiceT].to[TestService]
    bind[SolverServiceT].to[SolverService]
    ()
  }
}
