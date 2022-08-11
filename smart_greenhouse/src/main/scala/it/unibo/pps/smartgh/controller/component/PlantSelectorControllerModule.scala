package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component
import it.unibo.pps.smartgh.view.component.{BaseView, SelectPlantViewModule}
import it.unibo.pps.smartgh.model.plants
import monix.execution.Ack.Continue
import it.unibo.pps.smartgh.model.plants.Plant

/** Object that encloses the controller module for the plant selection. */
object PlantSelectorControllerModule:

  /** A trait that represents the controller for the plant selection scene. */
  trait PlantSelectorController extends SceneController:

    /** Method that requires to the controller to configure the available plants that can be chosen by the user. */
    def configureModelCallbacks(): Unit

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

      override def configureModelCallbacks(): Unit =
        configurePlantSelectionCallback()
        configurePlantInfoCallback()

      override def notifySelectedPlant(plantName: String): Unit =
        plantSelectorModel.selectPlant(plantName)

      override def notifyDeselectedPlant(plantName: String): Unit =
        try plantSelectorModel.deselectPlant(plantName)
        catch case e: NoSuchElementException => selectPlantView.showErrorMessage(e.getMessage)

      override def instantiateNextSceneMVC(baseView: BaseView): Unit =
        val environmentMVC = component.EnvironmentMVC(simulationMVC, baseView)
        simulationMVC.simulationController.environmentController = environmentMVC.environmentController
        selectPlantView.moveToNextScene(environmentMVC.environmentView)

      override def notifyStartSimulationClicked(): Unit =
        if plantSelectorModel.getPlantsSelectedName.nonEmpty then
          plantSelectorModel.startEmittingPlantsSelected()
          selectPlantView.setNewScene()
        else selectPlantView.showErrorMessage("At least one plant must be selected")

      private def configurePlantSelectionCallback(): Unit =
        plantSelectorModel.registerCallbackPlantSelection(
          (l: List[String]) => {
            selectPlantView.updateSelectedPlant(l)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        selectPlantView.showSelectablePlants(plantSelectorModel.getAllAvailablePlants)

      private def configurePlantInfoCallback(): Unit =
        var plantList: List[Plant] = List()
        plantSelectorModel.registerCallbackPlantInfo(
          (p: Plant) => {
            plantList = plantList :+ p
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => simulationMVC.simulationController.plantsSelected = plantList
        )
        selectPlantView.showSelectablePlants(plantSelectorModel.getAllAvailablePlants)

  /** Trait that encloses the controller for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
