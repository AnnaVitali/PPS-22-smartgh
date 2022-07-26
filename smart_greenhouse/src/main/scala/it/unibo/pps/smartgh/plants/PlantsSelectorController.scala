package it.unibo.pps.smartgh.plants

import it.unibo.pps.smartgh.view.component.SelectPlantsView

trait PlantsSelectorController:
  def view: SelectPlantsView
  def view_=(view: SelectPlantsView): Unit
  def configureAvailablePlants(): Unit
  def notifySelectedPlant(plantName: String): Unit
  def showSelectedPlant(): Unit

object PlantsSelectorController:

  def apply(): PlantsSelectorController =
    PlantsSelectorControllerImpl()

  private class PlantsSelectorControllerImpl extends PlantsSelectorController:
    val model = PlantSelector()
    var _view: SelectPlantsView = null

    override def view: SelectPlantsView = _view

    override def view_=(view: SelectPlantsView): Unit = _view = view

    override def configureAvailablePlants(): Unit =
      view.showSelectablePlants(model.getAllAvailablePlants)

    override def notifySelectedPlant(plantName: String): Unit =
      model.selectPlant(plantName)

    override def showSelectedPlant(): Unit =
      view.updateSelectedPlant(model.getPlantsSelectedName)
