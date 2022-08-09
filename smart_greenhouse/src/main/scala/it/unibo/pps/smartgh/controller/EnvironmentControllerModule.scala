package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.controller.TimeController
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC

/** Object that encloses the controller module to manage ambient environment values and simulation time. */
object EnvironmentControllerModule:

  /** A trait that represents the controller for environment values and time management, it also allows their display in
    * the view.
    */
  trait EnvironmentController:

    /** @return time controller. */
    val timeController: TimeController

    /** Method that notify the controller to start the simulation. */
    def startSimulation(): Unit

    /** Method that notify the controller to stop the simulation. */
    def stopSimulation(): Unit

    /** Method that notify the controller to request environment's model to update environment values.
      * @param hour
      *   time value, expressed in hours, to which the update request refers.
      */
    def notifyEnvironmentValuesChange(hour: Int): Unit

  /** Trait that represents the provider of the controller for environment values and time management. */
  trait Provider:
    val environmentController: EnvironmentController

  type Requirements = EnvironmentViewModule.Provider with EnvironmentModelModule.Provider

  /** Trait that represents the components of the controller for environment values and time management. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentController]] implementation. */
    class EnvironmentControllerImpl(selectedPlants: List[Plant]) extends EnvironmentController:

      val ghMVC = GreenHouseDivisionMVC(selectedPlants)
      environmentView.displayGreenHouseDivisionView(ghMVC.ghDivisionView)

      override val timeController: TimeController =
        TimeController(context.environmentModel.time, context.environmentView, this)

      override def startSimulation(): Unit =
        timeController.startSimulationTimer()
        ghMVC.setAreas(
          environmentModel.time.timer,
          Map(
            "temp" -> environmentModel.subjectTemperature,
            "lux" -> environmentModel.subjectLuminosity,
            "hum" -> environmentModel.subjectHumidity,
            "soilMoist" -> environmentModel.subjectSoilMoisture
          )
        )
        ghMVC.show()

      override def stopSimulation(): Unit = timeController.stopSimulationTimer()

      override def notifyEnvironmentValuesChange(hour: Int): Unit =
        context.environmentView.displayNameCity(environmentModel.city.nameCity)
        context.environmentModel.city.updateCurrentEnvironmentValues(hour)
        context.environmentView.displayEnvironmentValues(environmentModel.city.currentEnvironmentValues)
        notifySensors(environmentModel.city.currentEnvironmentValues)

      private def notifySensors(values: environmentModel.city.EnvironmentValues): Unit =
        context.environmentModel.subjectTemperature.onNext(values("temp_c").asInstanceOf[Double])
        context.environmentModel.subjectHumidity.onNext(values("humidity").asInstanceOf[BigInt].doubleValue)
        context.environmentModel.subjectLuminosity.onNext(values("lux").asInstanceOf[Int].toDouble)

  /** Trait that encloses the controller for environment values and time management. */
  trait Interface extends Provider with Component:
    self: Requirements =>
