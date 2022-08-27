package it.unibo.pps.smartgh.controller.component.areaParameter

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.AreaParameterView
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.scalactic.TripleEquals.convertToEqualizer

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/** Object that encloses the generic controller module for the area parameters. */
object AreaParameterController:

  private val UpdatePeriod = 2 seconds

  /** A trait that represents the generic controller for area parameters. */
  trait AreaParameterController:

    /** Get the optimal values of the parameter.
      * @return
      *   the optimal values
      */
    def getOptimalValues: String

    /** Initialize the view.
      * @param areaParametersView
      *   the view to initialize
      */
    def initializeView(areaParametersView: AreaParameterView): Unit

    /** Stop listening value updating. */
    def stopListening(): Unit

  /** Abstract class that represents a generic area parameter controller.
    * @param name
    *   the name of the parameter
    * @param areaModel
    *   the parameter area model
    * @param updateStateMessage
    *   the function that is called for updating area state and messages
    */
  abstract class AbstractAreaParameterController(
      private val name: String,
      private val areaModel: AreaModel,
      private val updateStateMessage: (String, Boolean) => Unit
  ) extends AreaParameterController:
    protected val sensor: ManageSensorImpl = areaModel.sensors.find(_.name.contentEquals(name)).orNull
    protected var timeoutUpd: Observable[Unit] = _

    protected var subscriptionTimeout: Cancelable = _

    override def getOptimalValues: String = "(%.2f%s, %.2f%s)".format(sensor.min, sensor.um, sensor.max, sensor.um)

    override def initializeView(view: AreaParameterView): Unit =
      timeoutUpd = Observable.interval(UpdatePeriod).map(_ => updateValues(view))
      subscriptionTimeout = timeoutUpd.subscribe()
      view.updateDescription(getOptimalValues)

    override def stopListening(): Unit = subscriptionTimeout.cancel()

    protected def updateValues(view: AreaParameterView): Unit =
      updateStateMessage(sensor.message, sensor.status === SensorStatus.ALARM)
      view.updateCurrentValue("%.2f %s".format(sensor.actualVal, sensor.um), sensor.status.toString)
