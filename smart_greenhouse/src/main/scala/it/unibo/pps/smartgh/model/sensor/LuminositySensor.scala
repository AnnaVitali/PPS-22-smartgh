package it.unibo.pps.smartgh.model.sensor

import monix.execution.Ack
import monix.reactive.Observable
import scala.util.Random

import scala.concurrent.Future

object LuminositySensor:

  def apply(initialLuminosity: Int): LuminositySensorImpl = LuminositySensorImpl(initialLuminosity)

  object FactoryFunctionsLuminositySensor:
    val computeLuminosityWithGreenhouseOpen: (currentValEnvironment: Int, currentLampBrightness: Int) => Int =
      ???
    val computeLuminosityWithGreenhouseCloseAndUnshild: (
        currentValEnvironment: Int,
        currentLampBrightness: Int
    ) => Int = ???
    val computeLuminosityWithGreenhouseCloseAndShild: (concurrentLampBrightness: Int) => Int = ???

  class LuminositySensorImpl(initialLuminosity: Int) extends Sensor:
    private val minPercentage = 0.1
    private val maxPercentage = 0.5
    private var currentValue: Int = (initialLuminosity - (minPercentage + (maxPercentage - minPercentage) * Random
      .nextDouble()) * initialLuminosity).toInt

    override def initialize(
        area: Observable[Any]
    ): Unit = ??? //registrazione callback sull'area quando viene prodotto un nuovo valore o cambia lo stato

    override def registerValueCallback(
        onNext: Double => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Unit = ???

    def getCurrentValue: Int =
      println(currentValue)
      currentValue
