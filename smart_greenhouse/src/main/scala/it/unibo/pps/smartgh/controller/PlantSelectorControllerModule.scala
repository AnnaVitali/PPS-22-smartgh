package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule

/** A trait that represents the controller for the scene of plant selection. */
object PlantSelectorControllerModule:
  trait PlantSelectorController:

    /** Method that requires to the controller to configure the available plant that can be choosen by the user. */
    def configureAvailablePlants(): Unit

    /** Method that notifies the controller that a new plant has been selected.
      * @param plantName
      *   the name of the plant that has been selected.
      */
    def notifySelectedPlant(plantName: String): Unit

    /** Method that notifies the controller that a plant has been deselected.
      * @param plantName
      *   the name of the plant that has been deselected.
      */
    def notifyDeselectedPlant(plantName: String): Unit

  trait Provider:
    val plantSelectorController: PlantSelectorController
  type Requirments = PlantSelectorModelModule.Provider with SelectPlantViewModule.Provider
  trait Component:
    context: Requirments =>
    class PlantSelectorControllerImpl extends PlantSelectorController:

      private val path = System.getProperty("user.home") + "/pps/"
      private val file = "plants.txt"
      private val prologFile = "plants.pl"
      private val uploader = UploadPlants
      uploader.writePrologFile(path, file, prologFile)

      override def configureAvailablePlants(): Unit =
        context.selectPlantView.showSelectablePlants(context.plantSelectorModel.getAllAvailablePlants())

      override def notifySelectedPlant(plantName: String): Unit =
        context.plantSelectorModel.selectPlant(plantName)
        context.selectPlantView.updateSelectedPlant(plantName)

      override def notifyDeselectedPlant(plantName: String): Unit =
        context.plantSelectorModel.deselectPlant(plantName)
        context.selectPlantView.updateDeselectedPlant(plantName)

  trait Interface extends Provider with Component:
    self: Requirments =>
