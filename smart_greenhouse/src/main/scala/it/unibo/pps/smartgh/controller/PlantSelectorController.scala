package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.plants.{PlantSelector, UploadPlants}
import it.unibo.pps.smartgh.view.component.SelectPlantView

/** A trait that represents the controller for the scene of plant selection. */
trait PlantSelectorController:
  /** Getter method for the view component.
    * @return
    *   the view associated to the controller.
    */
  def view: SelectPlantView

  /** Setter method for the view component.
    * @param view
    *   the view associated to the controller.
    */
  def view_=(view: SelectPlantView): Unit

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

/** Object that can be used to create a new instance of [[PlantSelectorController]]. */
object PlantSelectorController:

  /** Create a new [[PlantSelectorController]].
    * @return
    *   a new instance of [[PlantSelectorController]].
    */
  def apply(): PlantSelectorController =
    PlantSelectorControllerImpl()

  private class PlantSelectorControllerImpl extends PlantSelectorController:

    private val path = System.getProperty("user.home") + "/pps/"
    private val file = "plants.txt"
    private val prologFile = "plants.pl"
    private val uploader = UploadPlants
    uploader.writePrologFile(path, file, prologFile)
    val model = PlantSelector(path + prologFile)
    override var view: SelectPlantView = _

    override def configureAvailablePlants(): Unit =
      view.showSelectablePlants(model.getAllAvailablePlants())

    override def notifySelectedPlant(plantName: String): Unit =
      model.selectPlant(plantName)
      view.updateSelectedPlant(plantName)

    override def notifyDeselectedPlant(plantName: String): Unit =
      model.deselectPlant(plantName)
      view.updateDeselectedPlant(plantName)
