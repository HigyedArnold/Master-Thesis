package com.planr.main

import com.planr.core.DisjunctiveIntervals
import com.planr.core.util.NativeLibLoader._

object Main extends App {

  if (args.length == 1 && args(0) == "DEV") init(DEVELOPMENT)
  else init(PRODUCTION)

  run()

  def run(): Unit = {
    val disjunctiveIntervals = new DisjunctiveIntervals
    disjunctiveIntervals.search(0, 10, disjunctiveIntervals.blockedIntervals1, disjunctiveIntervals.requestIntervals1)
  }

}
