package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import scala.concurrent.duration.DurationInt

/** Object that encloses the generic controller module for the area parameters. */
object AreaParametersController:

  private val updatePeriod = 2.seconds

  /** A trait that represents the generic controller for area parameters. */
  trait AreaParametersController:
    def getOptimalValues: String
    def initializeView(areaParametersView: AreaParametersView): Unit
    def stopListening(): Unit

  abstract class AbstractAreaParametersController(
      name: String,
      areaModel: AreaModel,
      updateStateMessage: (String, Boolean) => Unit
  ) extends AreaParametersController:
    protected val sensor: ManageSensorImpl = areaModel.sensors.find(_.name.contentEquals(name)).orNull
    protected var timeoutUpd: Observable[Unit] = _

    protected var subscriptionTimeout: Cancelable = _

    override def getOptimalValues: String = "(%.2f%s, %.2f%s)".format(sensor.min, sensor.um, sensor.max, sensor.um)

    override def initializeView(view: AreaParametersView): Unit =
      timeoutUpd = Observable
        .interval(updatePeriod)
        .map(_ => updateValues(view))
      subscriptionTimeout = timeoutUpd.subscribe()
      view.updateDescription(getOptimalValues)

    override def stopListening(): Unit =
      subscriptionTimeout.cancel()

    protected def updateValues(view: AreaParametersView): Unit =
      updateStateMessage(sensor.message, sensor.status == SensorStatus.ALARM)
      view.updateCurrentValue("%.2f %s".format(sensor.actualVal, sensor.um), sensor.status.toString)
