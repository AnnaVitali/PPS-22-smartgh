package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, LoadingPlantViewModule}
import monix.execution.Ack.Continue
import it.unibo.pps.smartgh.mvc.component
import it.unibo.pps.smartgh.model.plants.Plant
import monix.execution.{Ack, Cancelable}
import concurrent.{Future, Promise}

object LoadingPlantControllerModule:

  trait LoadingPlantController extends SceneController:

    def setupBehaviour(): Unit

  trait Provider:

    val loadingPlantController: LoadingPlantController

  type Requirements = PlantSelectorModelModule.Provider with LoadingPlantViewModule.Provider

  trait Component:
    context: Requirements =>

    class LoadingPlantControllerImpl(simulationMVC: SimulationMVCImpl) extends LoadingPlantController:

      private var plantList: List[Plant] = List()
      private val progressIndicatorIncrement: Int => Double = maxDimension => (100 / maxDimension.toDouble) * 0.01
      private val onNextPlantEmitted: Plant => Future[Ack] = p => {
        plantList = plantList :+ p
        loadingPlantView.incrementProgressIndicator(
          plantList.size * progressIndicatorIncrement(plantSelectorModel.getPlantSelectedCount)
        )
        Continue
      }
      private val onCompletePlantsEmission: () => Unit = () => loadingPlantView.setupNextScene()

      override def setupBehaviour(): Unit =
        plantSelectorModel.registerCallbackPlantInfo(
          onNextPlantEmitted,
          (ex: Throwable) => ex.printStackTrace(),
          onCompletePlantsEmission
        )

      override def instantiateNextSceneMVC(baseView: BaseView): Unit =
        simulationMVC.simulationController.plantsSelected = plantList
        val environmentMVC = component.EnvironmentMVC(simulationMVC, baseView)
        simulationMVC.simulationController.environmentController = environmentMVC.environmentController
        loadingPlantView.moveToNextScene(environmentMVC.environmentView)

  trait Interface extends Provider with Component:
    self: Requirements =>
