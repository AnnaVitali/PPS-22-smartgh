package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView
import it.unibo.pps.smartgh.view.component.{BaseView, EnvironmentViewModule, FinishSimulationView}
import monix.eval.Task
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

/** Object that encloses the controller module to manage ambient environment values and to visualize of simulation time.
  */
object EnvironmentControllerModule:

  /** A trait that represents the controller to manage ambient environment values and to visualize of simulation time.
    */
  trait EnvironmentController:

    /** Method that notify the controller to start the simulation time and to display the division on areas. */
    def startSimulation(): Unit

    /** Method that notify the controller to stop the simulation time. */
    def stopSimulation(): Unit

    /** Method that notify the controller to request environment's model to update environment values, and to send
      * changes to view component and sensors.
      * @param hour
      *   time value, expressed in hours, to which the update request refers.
      */
    def notifyEnvironmentValuesChange(hour: Int): Unit

    /** Method to notify simulation controller to update velocity of the simulation time.
      * @param value
      *   value of the speed factor.
      */
    def updateVelocityTimer(value: Double): Unit

    /** Method to notify environment controller about time value change.
      * @param timeValue
      *   new time value
      */
    def notifyTimeValueChange(timeValue: String): Unit

    /** Method that asks the controller to finish the visualization of the simulation. */
    def finishSimulation(): Unit

    /** Gets the [[EnvironmentView]]
      * @return
      *   the [[EnvironmentView]]
      */
    def envView(): EnvironmentView

    /** Subscription to the timer value change
      * @param callback
      *   subscribe callback
      */
    def subscribeTimerValue(callback: String => Unit): Unit

    /** Restore environment after back button. */
    def backToEnvironment(): Unit

  /** Trait that represents the provider of the controller for environment values and time management. */
  trait Provider:
    /** The environment controller. */
    val environmentController: EnvironmentController

  /** The controller requirements. */
  type Requirements = EnvironmentViewModule.Provider with EnvironmentModelModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the controller for environment values management and time visualization.
    */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentController]] implementation. */
    class EnvironmentControllerImpl() extends EnvironmentController:
      private val subject = ConcurrentSubject[String](MulticastStrategy.publish)

      private val ghMVC: GreenHouseDivisionMVC.GreenHouseDivisionMVCImpl = GreenHouseDivisionMVC(
        simulationMVC.simulationController.plantsSelected,
        simulationMVC
      )
      environmentView.displayGreenHouseDivisionView(ghMVC.ghDivisionView)

      override def startSimulation(): Unit =
        simulationMVC.simulationController.startSimulationTimer()
        ghMVC.setAreas(
          Map(
            "temp" -> environmentModel.subjectTemperature,
            "lux" -> environmentModel.subjectLuminosity,
            "hum" -> environmentModel.subjectHumidity,
            "soilMoist" -> environmentModel.subjectSoilMoisture
          )
        )
        ghMVC.show()

      override def stopSimulation(): Unit = simulationMVC.simulationController.stopSimulationTimer()

      override def notifyEnvironmentValuesChange(hour: Int): Unit =
        environmentView.displayNameCity(environmentModel.environment.nameCity)
        environmentModel.environment.updateCurrentEnvironmentValues(hour)
        environmentView.displayEnvironmentValues(environmentModel.environment.currentEnvironmentValues)
        notifySensors(environmentModel.environment.currentEnvironmentValues)

      override def updateVelocityTimer(value: Double): Unit =
        simulationMVC.simulationController.updateVelocityTimer(value)

      override def notifyTimeValueChange(timeValue: String): Unit =
        Task {
          subject.onNext(timeValue)
          environmentView.displayElapsedTime(timeValue)
        }.executeAsync.runToFuture

      override def finishSimulation(): Unit =
        environmentView.finishSimulation()

      override def envView(): EnvironmentView = environmentView

      override def backToEnvironment(): Unit =
        environmentView.setBackButton()
        ghMVC.ghDivisionController.updateView()

      override def subscribeTimerValue(callback: String => Unit): Unit =
        subject.subscribe(
          s => {
            Task {
              callback(s)
            }.executeAsync.runToFuture
            Continue
          },
          _.printStackTrace(),
          () => {}
        )

      private def notifySensors(values: environmentModel.environment.EnvironmentValues): Unit =
        environmentModel.subjectTemperature.onNext(values("temp_c").asInstanceOf[Double])
        environmentModel.subjectHumidity.onNext(values("humidity").asInstanceOf[BigInt].doubleValue)
        environmentModel.subjectLuminosity.onNext(values("lux").asInstanceOf[Int].toDouble)

  /** Trait that encloses the controller for environment values management and time visualization. */
  trait Interface extends Provider with Component:
    self: Requirements =>
