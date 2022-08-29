package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView

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
        environmentView.displayNameCity(simulationMVC.simulationController.environment.nameCity)
        environmentModel.updateCurrentEnvironmentValues(hour)
        environmentView.displayEnvironmentValues(environmentModel.currentEnvironmentValues)
        notifySensors(environmentModel.currentEnvironmentValues)

      override def updateVelocityTimer(value: Double): Unit =
        simulationMVC.simulationController.updateVelocityTimer(value)

      override def notifyTimeValueChange(timeValue: String): Unit =
          environmentView.displayElapsedTime(timeValue)

      override def finishSimulation(): Unit =
        environmentView.finishSimulation()

      override def envView(): EnvironmentView = environmentView

      override def backToEnvironment(): Unit =
        environmentView.setBackButton()
        ghMVC.ghDivisionController.updateView()

      private def notifySensors(values: environmentModel.EnvironmentValues): Unit =
        environmentModel.subjectTemperature.onNext(values("temp_c").asInstanceOf[Double])
        environmentModel.subjectHumidity.onNext(values("humidity").asInstanceOf[BigInt].doubleValue)
        environmentModel.subjectLuminosity.onNext(values("lux").asInstanceOf[Int].toDouble)
        environmentModel.subjectSoilMoisture.onNext(values("precipitation").asInstanceOf[Double])

  /** Trait that encloses the controller for environment values management and time visualization. */
  trait Interface extends Provider with Component:
    self: Requirements =>
