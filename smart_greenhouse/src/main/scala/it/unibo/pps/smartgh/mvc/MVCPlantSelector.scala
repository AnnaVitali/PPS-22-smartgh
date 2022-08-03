package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule
import it.unibo.pps.smartgh.model.plants.{PlantSelectorModelModule, UploadPlants}
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.{BaseView, SelectPlantViewModule}
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.Model
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule.Controller
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.View

object MVCPlantSelector
    extends PlantSelectorModelModule.Interface
    with SelectPlantViewModule.Interface
    with PlantSelectorControllerModule.Interface:
  override var model: Model = _
  override var view: View = _
  override var controller: Controller = _

  def setupModel(): Unit =
    val path = System.getProperty("user.home") + "/pps/"
    val file = "plants.txt"
    val prologFile = "plants.pl"
    val uploader = UploadPlants
    uploader.writePrologFile(path, file, prologFile)
    model = PlantSelectorModelImpl(path + prologFile)

  def setupView(simultationView: SimulationView, baseView: BaseView): Unit =
    view = SelectPlantViewImpl(simultationView, baseView)

  def setupController(): Unit =
    controller = PlantSelectorControllerImpl()
    controller.configureAvailablePlants()
