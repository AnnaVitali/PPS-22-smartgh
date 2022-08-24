package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaSensorHelper.AreaSensorHelperImpl
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.area.{AreaComponentsState, AreaGatesState}
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.sensor.*
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.{MulticastStrategy, Observable}
import org.scalactic.TripleEquals.convertToEqualizer

import scala.math.BigDecimal

/** Implementation of the [[AreaModelModule]]. */
object AreaModelModule:
  enum AreaStatus:
    case ALARM, NORMAL
  export AreaStatus.*

  /** This trait exposes the methods for managing the area model. */
  trait AreaModel:
    /** Set the status of the area.
      * @param s
      *   new [[AreaStatus]]
      */
    def status_=(s: AreaStatus): Unit
    /** Status of the area. */
    def status: AreaStatus
    /** [[Plant]] grown in the area. */
    val plant: Plant
    /** A helper for sensors */
    val areaSensorHelper: AreaSensorHelperImpl
    /** Sensor of the area. */
    val sensors: List[ManageSensorImpl]
    /** @return the map of the actual sensors values */
    def sensorValues(): Map[String, Double]

    /** Method that can be called to obtain the [[Observable]] associated to the status of an area.
      * @return
      *   the [[Observable]] associated to the status of an area.
      */
    def changeStatusObservable(): Observable[AreaStatus]

    /** Set the sensors' subjects
      * @param subjects
      *   the subject of the sensor.
      */
    def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit

    /** Update the [[AreaGatesState]] due to a user action.
      * @param state
      *   the new state for the gate
      */
    def updGateState(state: AreaGatesState): Unit

    /** Update the [[AreaShieldState]]due to a user action.
      * @param state
      *   new shield state
      */
    def updShieldState(state: AreaShieldState): Unit

    /** Update the [[AreaAtomiseState]] due to a user action.
      * @param state
      *   new atomize state
      */
    def updAtomizeState(state: AreaAtomiseState): Unit

    /** Update the [[AreaVentilationState]] due to a user action.
      * @param state
      *   new ventilation state
      */
    def updVentilationState(state: AreaVentilationState): Unit

    /** Update the [[AreaHumidityState]] due to a user action.
      * @param state
      *   new humidity state
      */
    def updHumidityAction(state: AreaHumidityState): Unit

    /** Update the lamps' brightness due to a user action.
      * @param value
      *   new brightness value
      */
    def updBrightnessOfLamp(value: Double): Unit

    /** Update the temperature value due to a user action.
      * @param value
      *   new temperature value
      */
    def updTemperature(value: Double): Unit

    /** Get a copy of the area's component
      * @return
      *   the area's component
      */
    def getAreaComponent: AreaComponentsStateImpl

  /** A trait for defining the model instance. */
  trait Provider:
    /** The area model. */
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.
      * @param plant
      *   [[Plant]] grown in the area.
      * @param addTimerCallback
      *   the callback for the timer.
      */
    class AreaImpl(override val plant: Plant, val addTimerCallback: (f: String => Unit) => Unit) extends AreaModel:

      private var _status: AreaStatus = NORMAL
      private val subject = ConcurrentSubject[AreaStatus](MulticastStrategy.publish)
      private val optimalValueToDouble: Map[String, Double] =
        plant.optimalValues.map((k, v) => (k, v.toString.toDouble))
      private val areaComponentState = AreaComponentsState()
      private val subjectComponentsState = ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

      override val areaSensorHelper: AreaSensorHelperImpl = AreaSensorHelperImpl(areaComponentState, addTimerCallback)
      override val sensors: List[ManageSensorImpl] = areaSensorHelper.manageSensorList(optimalValueToDouble)

      status = areaSensorHelper.configSensors(sensors, checkAlarm, subjectComponentsState)

      override def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
        areaSensorHelper.setSensorSubjects(subjects)

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        Task {
          _status = s
          subject.onNext(_status)
        }.executeAsync.runToFuture

      override def sensorValues(): Map[String, Double] =
        sensors.map(ms => (ms.name, ms.actualVal)).toMap

      override def changeStatusObservable(): Observable[AreaStatus] = subject

      override def updTemperature(value: Double): Unit =
        areaComponentState.temperature = value
        notifySensorStatusChange()

      override def updBrightnessOfLamp(value: Double): Unit =
        areaComponentState.brightnessOfTheLamps = value
        notifySensorStatusChange()

      override def updHumidityAction(state: AreaHumidityState): Unit =
        areaComponentState.humidityActions = state
        notifySensorStatusChange()

      override def updAtomizeState(state: AreaAtomiseState): Unit =
        areaComponentState.atomisingState = state
        notifySensorStatusChange()

      override def updGateState(state: AreaGatesState): Unit =
        areaComponentState.gatesState = state
        notifySensorStatusChange()

      override def updShieldState(state: AreaShieldState): Unit =
        areaComponentState.shieldState = state
        notifySensorStatusChange()

      override def updVentilationState(state: AreaVentilationState): Unit =
        areaComponentState.ventilationState = state
        notifySensorStatusChange()

      override def getAreaComponent: AreaComponentsStateImpl = areaComponentState.copy()

      private def notifySensorStatusChange(): Unit =
        subjectComponentsState.onNext(areaComponentState)

      private def checkAlarm(ms: ManageSensorImpl): Unit =
        if (ms.actualVal compareTo ms.min) < 0 || (ms.actualVal compareTo ms.max) > 0 then
          if status === NORMAL then status = ALARM
          ms.status = SensorStatus.ALARM
        else
          ms.status = SensorStatus.NORMAL
          if sensors.forall(ms => ms.status === SensorStatus.NORMAL) then status = NORMAL

  /** Trait that combine provider and component for area model. */
  trait Interface extends Provider with Component
