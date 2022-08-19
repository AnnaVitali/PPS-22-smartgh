package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import monix.execution.Cancelable
import monix.reactive.Observable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global

object AreaParameterController:
  trait AreaParameterController:
    def getOptimalValues: (Double, Double)
    def initializeView(): Unit
    def stopListening(): Unit
    def updateValues(): Unit

  abstract class AbstractAreaParameterController(areaModel: AreaModel, name: String) extends AreaParameterController:
    protected val sensor: ManageSensorImpl = areaModel.sensors.find(_.name.contentEquals(name)).orNull
    protected var timeoutUpd: Observable[Unit] = Observable
      .interval(3.seconds)
      .map(_ => updateValues())

    protected var subscriptionTimeout: Cancelable = _

    override def initializeView(): Unit =
      subscriptionTimeout = timeoutUpd.subscribe()

    override def stopListening(): Unit =
      subscriptionTimeout.cancel()
