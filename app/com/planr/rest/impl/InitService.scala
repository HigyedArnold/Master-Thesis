package com.planr.rest.impl

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.google.inject.Inject
import com.planr.api.async.SolverServiceT
import com.planr.api.sync.InitServiceT
import com.planr.solver.actor.SolverActor
import com.planr.solver.util.NativeLibLoader
import play.api.{Configuration, Logger}

class InitService @Inject() (config: Configuration, actorSystem: ActorSystem) extends InitServiceT {

  private val logger = Logger(this.getClass)
  init()

  override def init(): Unit = {
    if (NativeLibLoader.init(NativeLibLoader.DEVELOPMENT)) {
      actorSystem.actorOf(RoundRobinPool(config.get[Int]("solver.actor.count")).props(Props(new SolverActor)).withDispatcher("solver.dispatcher"), SolverServiceT.actorName)
      logger.info("Server initialization successful!")
    }
    ()
  }
}
