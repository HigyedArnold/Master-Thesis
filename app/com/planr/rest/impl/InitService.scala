package com.planr.rest.impl

import akka.actor.ActorSystem
import com.google.inject.Inject
import com.planr.api.sync.InitServiceT
import com.planr.solver.util.NativeLibLoader
import play.api.Configuration

class InitService @Inject() (config: Configuration, actorSystem: ActorSystem) extends InitServiceT {

  init()

  override def init(): Unit = {
    // Load natives
    if (NativeLibLoader.init(NativeLibLoader.DEVELOPMENT)) {
      // Initialize actors

    }
    ()
  }
}
