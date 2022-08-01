package it.unibo.pps.smartgh.plants

import it.unibo.pps.smartgh.view.component.SelectPlantView

trait PlantSelectorController:
  def view: SelectPlantView
  def view_=(view: SelectPlantView): Unit
  def configureAvailablePlants(): Unit
  def notifySelectedPlant(plantName: String): Unit
  def notifyDeselectedPlant(plantName: String): Unit

object PlantSelectorController:

  def apply(): PlantSelectorController =
    PlantSelectorControllerImpl()

  private class PlantSelectorControllerImpl extends PlantSelectorController:

    private val path = System.getProperty("user.home") + "/pps/"
    private val file = "plants.txt"
    private val prologFile = "plants.pl"
    private val uploader = UploadPlants
    uploader.writePrologFile(path, file, prologFile)
    val model = PlantSelector(path + prologFile)
    var _view: SelectPlantView = _

    override def view: SelectPlantView = _view

    override def view_=(view: SelectPlantView): Unit = _view = view

    override def configureAvailablePlants(): Unit =
      view.showSelectablePlants(model.getAllAvailablePlants())

    override def notifySelectedPlant(plantName: String): Unit =
      model.selectPlant(plantName)
      view.updateSelectedPlant(plantName)

    override def notifyDeselectedPlant(plantName: String): Unit =
      model.deselectPlant(plantName)
      view.updateDeselectedPlant(plantName)
