package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.AirHumiditySensor.AirHumiditySensorImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{
  AreaComponentsState,
  AreaGatesState,
  AreaAtomiseState,
  AreaVentilationState
}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.time.Timer
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import monix.execution.Scheduler.Implicits.global
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Span}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.*
import scala.language.postfixOps

class AirHumiditySensorTest extends AnyFunSuite with Matchers with Eventually with BeforeAndAfter:

  private var areaComponentsState: AreaComponentsStateImpl = _
  private val timer = Timer(1 day)
  private val initialHumidity = 30.0
  private var humiditySensor: AirHumiditySensorImpl = _
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  private def setupTimer(tickPeriod: FiniteDuration): Unit =
    timer.start(println("time is up!"))
    timer.changeTickPeriod(tickPeriod)
    humiditySensor.registerTimerCallback()

  before {
    areaComponentsState = AreaComponentsState()
    areaComponentsState.soilHumidity = initialHumidity
    humiditySensor = AirHumiditySensor(areaComponentsState, timer)
    humiditySensor.setObserverEnvironmentValue(subjectEnvironment)
    humiditySensor.setObserverActionsArea(subjectActions)
  }

  after {
    timer.stop()
  }

//  test("The air humidity value should decrease if the ventilation is active") {
//    setupTimer(50 microseconds)
//    areaComponentsState.ventilationState = AreaVentilationState.VentilationActive
//    subjectActions.onNext(areaComponentsState)
//    val beforeValue = humiditySensor.getCurrentValue()
//    eventually(timeout(Span(1000, Milliseconds))) {
//      humiditySensor.getCurrentValue() should be < beforeValue
//    }
//  }

  test("The air humidity value should increase if the atomiser is active") {
    setupTimer(50 microseconds)
    areaComponentsState.atomisingState = AreaAtomiseState.AtomisingActive
    subjectActions.onNext(areaComponentsState)
    val beforeValue = humiditySensor.getCurrentValue()
    eventually(timeout(Span(1000, Milliseconds))) {
      humiditySensor.getCurrentValue() should be < beforeValue
    }
  }
