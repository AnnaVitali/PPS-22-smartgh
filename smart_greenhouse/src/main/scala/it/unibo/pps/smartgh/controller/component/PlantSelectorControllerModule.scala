package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component
import it.unibo.pps.smartgh.view.component.{BaseView, SelectPlantViewModule}
import it.unibo.pps.smartgh.model.plants
import monix.execution.Ack.Continue
import it.unibo.pps.smartgh.model.plants.Plant

import concurrent.{Future, Promise}
import monix.execution.{Ack, Cancelable}
import it.unibo.pps.smartgh.mvc.component.EnvironmentMVC.EnvironmentMVCImpl
import it.unibo.pps.smartgh.mvc.component.LoadingPlantMVC

/** Object that encloses the controller module for the plant selection. */
object PlantSelectorControllerModule:

  /** A trait that represents the controller for the plant selection scene. */
  trait PlantSelectorController extends SceneController:

    /** Method that requires to the controller to setup the working environment.*/
    def setupBehaviour(): Unit

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

      private val onNextSelection: List[String] => Future[Ack] = l => {
        selectPlantView.updateSelectedPlant(l)
        Continue
      }
      private val onSelectionError: Throwable => Unit = ex => selectPlantView.showErrorMessage(ex.getMessage)

      override def setupBehaviour(): Unit =
        configurePlantSelectionCallback()
        selectPlantView.showSelectablePlants(plantSelectorModel.getAllAvailablePlants)

      override def notifySelectedPlant(plantName: String): Unit =
        plantSelectorModel.selectPlant(plantName)

      override def notifyDeselectedPlant(plantName: String): Unit =
        try plantSelectorModel.deselectPlant(plantName)
        catch case e: NoSuchElementException => selectPlantView.showErrorMessage(e.getMessage)

      override def instantiateNextSceneMVC(baseView: BaseView): Unit =
        if plantSelectorModel.getPlantsSelectedName.nonEmpty then
          val loadingPlantMVC = LoadingPlantMVC(simulationMVC, plantSelectorModel, baseView)
          selectPlantView.moveToNextScene(loadingPlantMVC.loadingPlantView)
          plantSelectorModel.startEmittingPlantsSelected()
        else selectPlantView.showErrorMessage("At least one plant must be selected")

      private def configurePlantSelectionCallback(): Unit =
        plantSelectorModel.registerCallbackPlantSelection(onNextSelection, onSelectionError, () => {})

  /** Trait that encloses the controller for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
