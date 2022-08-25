package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.sensor.LuminositySensor.LuminositySensorImpl
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task.timer
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import org.apache.commons.lang3.time.DurationFormatUtils
import org.scalactic.TripleEquals.convertToEqualizer
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.must.Matchers.mustEqual
import org.scalatest.time.{Milliseconds, Span}

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests to verify that the [[AreaModelModule]] works correctly. */
class AreaTest extends AnyFunSuite with AreaModelModule.Interface with Matchers:

  private val subject = ConcurrentSubject[String](MulticastStrategy.publish)
  private val timer = Timer(1 day)
  timer.start(
    t => subject.onNext(DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)),
    println("time is up!")
  )
  override val areaModel: AreaModelModule.AreaModel = AreaImpl(
    Plant("lemon", "citrus limon"),
    (callback: String => Unit) =>
      subject.subscribe(
        (s: String) => {
          callback(s)
          Continue
        },
        (ex: Throwable) => ex.printStackTrace(),
        () => {}
      )
  )

  test("After create an area in which there is a lemon plant, The area plant's name must be lemon") {
    areaModel.plant.name mustEqual "lemon"
  }

  test(
    "After create an area its state must be equal to Normal if all sensors status is Normal else must be equal to alarm"
  ) {
    if areaModel.sensors.forall(_.status === SensorStatus.NORMAL) then
      eventually(timeout(Span(8000, Milliseconds))) {
        areaModel.status mustEqual AreaModelModule.AreaStatus.NORMAL
      }
    else
      eventually(timeout(Span(8000, Milliseconds))) {
        areaModel.status mustEqual AreaModelModule.AreaStatus.ALARM
      }
  }

  test("An area must have 4 sensors") {
    areaModel.sensors.size mustEqual 4
  }

  test("If a user close the gate in an area, the gate area component must be with status Close") {
    areaModel.updGateState(AreaGatesState.Close)
    areaModel.getAreaComponent.gatesState mustEqual AreaGatesState.Close
  }

  test(
    "If a user before close the gate and then open it in an area, the gate area component must be with status Open"
  ) {
    areaModel.updGateState(AreaGatesState.Close)
    areaModel.getAreaComponent.gatesState mustEqual AreaGatesState.Close
    areaModel.updGateState(AreaGatesState.Open)
    areaModel.getAreaComponent.gatesState mustEqual AreaGatesState.Open
  }

  test("If a user shield an area, the shield area component must be with status Down") {
    areaModel.updShieldState(AreaShieldState.Down)
    areaModel.getAreaComponent.shieldState mustEqual AreaShieldState.Down
  }

  test("If a user before shield the area and then unshielded it, the shield area component must be with status Up") {
    areaModel.updShieldState(AreaShieldState.Down)
    areaModel.getAreaComponent.shieldState mustEqual AreaShieldState.Down
    areaModel.updShieldState(AreaShieldState.Up)
    areaModel.getAreaComponent.shieldState mustEqual AreaShieldState.Up
  }

  test("If a user set the temperature = 10 then the area temperature must be equal to 10") {
    areaModel.updTemperature(10.0)
    areaModel.getAreaComponent.temperature mustEqual 10.0
  }

  test(
    "If a user atomize the area, then its state must be AtomizeActive"
  ) {
    areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)
    areaModel.getAreaComponent.atomisingState mustEqual AreaAtomiseState.AtomisingActive
  }

  test(
    "If a user before atomize the area and then stop it, the area state state must be AtomizeInactive"
  ) {
    areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)
    areaModel.getAreaComponent.atomisingState mustEqual AreaAtomiseState.AtomisingActive
    areaModel.updAtomizeState(AreaAtomiseState.AtomisingInactive)
    areaModel.getAreaComponent.atomisingState mustEqual AreaAtomiseState.AtomisingInactive
  }

  test("If a user set the lamp brightness to 40000 then the area lamp brightness must be 40000") {
    areaModel.updBrightnessOfLamp(40000)
    areaModel.getAreaComponent.brightnessOfTheLamps mustEqual 40000
  }

  test("if a sensor has the current value out of the optimal values range its status must be ALARM") {
    import it.unibo.pps.smartgh.model.sensor.SensorStatus
    import monix.execution.Scheduler.Implicits.global
    areaModel.updBrightnessOfLamp(0)
    val lumSensor = areaModel.sensors.find(_.name === "Brightness").orNull
    val subjectEnvironment: ConcurrentSubject[Double, Double] =
      ConcurrentSubject[Double](MulticastStrategy.publish)
    areaModel.setSensorSubjects(Map("lux" -> subjectEnvironment))
    subjectEnvironment.onNext(0)
    eventually(timeout(Span(6000, Milliseconds))) {
      lumSensor.status mustEqual SensorStatus.ALARM
    }
  }
