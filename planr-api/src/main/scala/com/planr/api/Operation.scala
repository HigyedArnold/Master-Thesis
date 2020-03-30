package com.planr.api

import com.planr.api.OperationType.OperationType

case class Operation(key: String, name: String, opType: OperationType, duration: Long, resourceKeys: List[String])
