package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.{BaseView, EnvironmentViewModule, FinishSimulationView}
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.controller.SimulationControllerModule.*
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl

/** Object that encloses the controller module to manage ambient environment values and to visualize of simulation time. */
object EnvironmentControllerModule:

  /** A trait that represents the controller to manage ambient environment values and to visualize of simulation time. */
  trait EnvironmentController:

    /** Method that notify the controller to start the simulation time and to display the division on areas. */
    def startSimulation(): Unit

    /** Method that notify the controller to stop the simulation time. */
    def stopSimulation(): Unit

    /** Method that notify the controller to request environment's model to update environment values, and to send changes to view component and sensors.
      * @param hour
      *   time value, expressed in hours, to which the update request refers.
      */
    def notifyEnvironmentValuesChange(hour: Int): Unit

    /**
      * Method to notify simulation controller to update velocity of the simulation time.
      * @param value value of the speed factor.
      */
    def updateVelocityTimer(value: Double): Unit

    /**
      * Method to notify environment controller about time value change.
      * @param timeValue new time value
      */
    def notifyTimeValueChange(timeValue: String): Unit

    /**
      * Method that asks the controller to finish the visualization of the simulation.
      */
    def finishSimulation(): Unit

    /** Method that asks the controller to instantiate the next MVC, for the next scene.
      * @param baseView
      *   the template view for the next scene.
      */
    def nextMVC(baseView: BaseView): Unit

  /** Trait that represents the provider of the controller for environment values and time management. */
  trait Provider:
    /** The environment controller. */
    val environmentController: EnvironmentController

  /** The controller requirements. */
  type Requirements = EnvironmentViewModule.Provider
    with EnvironmentModelModule.Provider
    with SimulationControllerModule.Provider

  /** Trait that represents the components of the controller for environment values management and time visualization. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentController]] implementation.
      * @param simulationMVC
      *   the simulationMVC of the application.
      * */
    class EnvironmentControllerImpl(simulationMVC: SimulationMVCImpl) extends EnvironmentController:

      val ghMVC: GreenHouseDivisionMVC.GreenHouseDivisionMVCImpl = GreenHouseDivisionMVC(simulationController.plantsSelected)
      environmentView.displayGreenHouseDivisionView(ghMVC.ghDivisionView)

      override def startSimulation(): Unit =
        simulationController.startSimulationTimer()
        ghMVC.setAreas(
          simulationController.timer,
          Map(
            "temp" -> environmentModel.subjectTemperature,
            "lux" -> environmentModel.subjectLuminosity,
            "hum" -> environmentModel.subjectHumidity,
            "soilMoist" -> environmentModel.subjectSoilMoisture
          )
        )
        ghMVC.show()

      override def stopSimulation(): Unit = simulationController.stopSimulationTimer()

      override def notifyEnvironmentValuesChange(hour: Int): Unit =
        context.environmentView.displayNameCity(environmentModel.environment.nameCity)
        context.environmentModel.environment.updateCurrentEnvironmentValues(hour)
        context.environmentView.displayEnvironmentValues(environmentModel.environment.currentEnvironmentValues)
        notifySensors(environmentModel.environment.currentEnvironmentValues)

      override def updateVelocityTimer(value: Double): Unit =
        simulationController.updateVelocityTimer(value)

      override def notifyTimeValueChange(timeValue: String): Unit =
        context.environmentView.displayElapsedTime(timeValue)

      override def finishSimulation(): Unit =
        context.environmentView.finishSimulation()

      override def nextMVC(baseView: BaseView): Unit =
        context.environmentView.moveToNextScene(FinishSimulationView(simulationMVC, baseView))

      private def notifySensors(values: environmentModel.environment.EnvironmentValues): Unit =
        context.environmentModel.subjectTemperature.onNext(values("temp_c").asInstanceOf[Double])
        context.environmentModel.subjectHumidity.onNext(values("humidity").asInstanceOf[BigInt].doubleValue)
        context.environmentModel.subjectLuminosity.onNext(values("lux").asInstanceOf[Int].toDouble)

  /** Trait that encloses the controller for environment values management and time visualization. */
  trait Interface extends Provider with Component:
    self: Requirements =>
