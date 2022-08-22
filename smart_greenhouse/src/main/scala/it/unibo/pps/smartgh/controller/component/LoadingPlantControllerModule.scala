package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, LoadingPlantViewModule}
import monix.execution.Ack.Continue
import it.unibo.pps.smartgh.mvc.component
import it.unibo.pps.smartgh.model.plants.Plant
import monix.execution.{Ack, Cancelable}
import concurrent.{Future, Promise}

/** Object that enclose the controller module for loading the plants data. */
object LoadingPlantControllerModule:
  /** A trait that represents the controller for loading the plants data. */
  trait LoadingPlantController extends SceneController:

    /** Method that requires to the controller to setup the working environment. */
    def setupBehaviour(): Unit

  /** Trait that represents the provider of the controller for loading the plants data. */
  trait Provider:

    /** The loading plant controller. */
    val loadingPlantController: LoadingPlantController

  /** The requirements for the controller. */
  type Requirements = PlantSelectorModelModule.Provider with LoadingPlantViewModule.Provider

  /** Trait that represents the components of the controller for loading the plants data. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[LoadingPlantController]] implementation.
      * @param simulationMVC
      *   the simulationMVC of the application.
      */
    class LoadingPlantControllerImpl(simulationMVC: SimulationMVCImpl) extends LoadingPlantController:

      private var plantList: Vector[Plant] = Vector.empty
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
        simulationMVC.simulationController.plantsSelected = plantList.toList
        val environmentMVC = component.EnvironmentMVC(simulationMVC, baseView)
        loadingPlantView.moveToNextScene(environmentMVC.environmentView)

  trait Interface extends Provider with Component:
    self: Requirements =>
