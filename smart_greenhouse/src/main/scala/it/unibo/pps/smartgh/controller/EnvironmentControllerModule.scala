package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.{BaseView, EnvironmentViewModule, FinishSimulationView}
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.controller.SimulationControllerModule.*
import it.unibo.pps.smartgh.controller.SimulationControllerModule.SimulationController
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl

/** Object that encloses the controller module to manage ambient environment values and simulation time. */
object EnvironmentControllerModule:

  /** A trait that represents the controller for environment values and time management, it also allows their display in
    * the view.
    */
  trait EnvironmentController:

    /** @return time controller. */
    //val timeController: TimeController

    /** Method that notify the controller to start the simulation. */
    def startSimulation(): Unit

    /** Method that notify the controller to stop the simulation. */
    def stopSimulation(): Unit

    /** Method that notify the controller to request environment's model to update environment values.
      * @param hour
      *   time value, expressed in hours, to which the update request refers.
      */
    def notifyEnvironmentValuesChange(hour: Int): Unit

    def updateVelocityTimer(value: Double): Unit

    def notifyTimeValueChange(timeValue: String) : Unit

    def finishSimulation() : Unit

    def nextMVC(baseView: BaseView): Unit

  /** Trait that represents the provider of the controller for environment values and time management. */
  trait Provider:
    val environmentController: EnvironmentController

  type Requirements = EnvironmentViewModule.Provider
    with EnvironmentModelModule.Provider
    with SimulationControllerModule.Provider

  /** Trait that represents the components of the controller for environment values and time management. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentController]] implementation. */
    class EnvironmentControllerImpl(simulationMVC : SimulationMVCImpl) extends EnvironmentController:

      val ghMVC = GreenHouseDivisionMVC(simulationController.plantsSelected)
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

  /** Trait that encloses the controller for environment values and time management. */
  trait Interface extends Provider with Component:
    self: Requirements =>
