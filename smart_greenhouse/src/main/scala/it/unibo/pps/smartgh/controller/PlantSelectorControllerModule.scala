package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule
import monix.execution.Ack.{Continue, Stop}
import monix.execution.Scheduler.Implicits.global

/** Object that encloses the controller module for the plant selection. */
object PlantSelectorControllerModule:

  /** A trait that represents the controller for the scene of plant selection. */
  trait PlantSelectorController:

    /** Method that requires to the controller to configure the available plant that can be choosen by the user. */
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

    /** Method tha notifies the controller that the start simulation button has been clicked */
    def notifyStartSimulationClicked(): Unit

  /** Trait that represents the provider of the controller for the plant selection. */
  trait Provider:
    val plantSelectorController: PlantSelectorController
  type Requirments = PlantSelectorModelModule.Provider with SelectPlantViewModule.Provider

  /** Trait that represents the components of the controller for the plant selection. */
  trait Component:
    context: Requirments =>

    /** Class that contains the [[PlantSelectorController]] implementation. */
    class PlantSelectorControllerImpl extends PlantSelectorController:

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
        catch case e: NoSuchElementException => context.selectPlantView.showErrorMessage(e.getMessage())

      override def notifyStartSimulationClicked(): Unit =
        if context.plantSelectorModel.getPlantsSelectedName().size != 0 then
          context.selectPlantView.moveToTheNextScene()
        else selectPlantView.showErrorMessage("At least one plant must be selected")

  /** Trait that encloses the controller for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirments =>
