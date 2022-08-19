package it.unibo.pps.smartgh.controller.component.areaParameters

import monix.execution.Cancelable
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global

object AreaParameterController:
  trait AreaParameterController:
    def getOptimalValues: (Double, Double)
    def initializeView(): Unit
    def stopListening(): Unit

  abstract class AbstractAreaParameterController extends AreaParameterController:
    protected var timeoutUpd: Observable[Unit] = _
    protected var subscriptionTimeout: Cancelable = _

    override def initializeView(): Unit =
      subscriptionTimeout = timeoutUpd.subscribe()

    override def stopListening(): Unit =
      subscriptionTimeout.cancel()
