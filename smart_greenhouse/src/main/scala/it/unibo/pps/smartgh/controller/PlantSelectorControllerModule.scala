package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.view.component.{BaseView, SelectPlantViewModule}
import it.unibo.pps.smartgh.model.city.Environment
import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import monix.execution.Ack.{Continue, Stop}
import monix.execution.Scheduler.Implicits.global
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.EnvironmentMVC

/** Object that encloses the controller module for the plant selection. */
object PlantSelectorControllerModule:

  /** A trait that represents the controller for the plant selection scene. */
  trait PlantSelectorController:

    /** Method that requires to the controller to configure the available plants that can be chosen by the user. */
    def configureAvailablePlants(): Unit

    /** Method that notifies the controller that a plant has been selected.
      * @param plantName
      *   the name of the plant that has been selected.
      */
    def notifySelectedPlant(plantName: String): Unit

    /** Method that notifies the controller that a plant has been deselected.
      * @param plantName
      *   the name of the plant that has been deselected.
      */
    def notifyDeselectedPlant(plantName: String): Unit

    /** Method that notifies the controller that the start simulation button has been clicked */
    def notifyStartSimulationClicked(): Unit

    /** Method that asks the controller to instantiate the next MVC, for the next scene.
      * @param baseView
      *   the template view for the next scene
      */
    def nextMVC(baseView: BaseView): Unit

  /** Trait that represents the provider of the controller for the plant selection. */
  trait Provider:
    /** The plant selector controller. */
    val plantSelectorController: PlantSelectorController

  /** The requirements for the controller. */
  type Requirements = PlantSelectorModelModule.Provider with SelectPlantViewModule.Provider

  /** Trait that represents the components of the controller for the plant selection. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[PlantSelectorController]] implementation.
      * @param simulationMVC
      *   the simulationMVC of the application.
      */
    class PlantSelectorControllerImpl(simulationMVC: SimulationMVCImpl) extends PlantSelectorController:

      override def configureAvailablePlants(): Unit =
        context.plantSelectorModel.registerCallback(
          (l: List[String]) => {
            context.selectPlantView.updateSelectedPlant(l)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        context.selectPlantView.showSelectablePlants(context.plantSelectorModel.getAllAvailablePlants())

      override def notifySelectedPlant(plantName: String): Unit =
        context.plantSelectorModel.selectPlant(plantName)

      override def notifyDeselectedPlant(plantName: String): Unit =
        try context.plantSelectorModel.deselectPlant(plantName)
        catch case e: NoSuchElementException => context.selectPlantView.showErrorMessage(e.getMessage)

      override def nextMVC(baseView: BaseView): Unit =
        val environmentMVC = EnvironmentMVC(simulationMVC, baseView)
        simulationMVC.simulationController.environmentController = environmentMVC.environmentController
        context.selectPlantView.moveToNextScene(environmentMVC.environmentView)

      override def notifyStartSimulationClicked(): Unit =
        if context.plantSelectorModel.getPlantsSelectedName().nonEmpty then
          simulationMVC.simulationController.plantsSelected = context.plantSelectorModel.getPlantsSelected()
          context.selectPlantView.setNewScene()
        else selectPlantView.showErrorMessage("At least one plant must be selected")

  /** Trait that encloses the controller for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
