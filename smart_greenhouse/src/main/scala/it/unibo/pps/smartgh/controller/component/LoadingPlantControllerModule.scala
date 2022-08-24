package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.plants.{Plant, PlantSelectorModelModule}
import it.unibo.pps.smartgh.mvc.{SimulationMVC, component}
import it.unibo.pps.smartgh.view.component.LoadingPlantViewModule
import monix.execution.Ack.Continue
import monix.execution.Ack

import scala.concurrent.Future

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
  type Requirements = PlantSelectorModelModule.Provider with LoadingPlantViewModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the controller for loading the plants data. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[LoadingPlantController]] implementation. */
    class LoadingPlantControllerImpl() extends LoadingPlantController:

      private var plantList: Vector[Plant] = Vector.empty
      private val progressIndicatorIncrement: Int => Double = maxDimension => (100 / maxDimension.toDouble) * 0.01
      private val onNextPlantEmitted: Plant => Future[Ack] = p => {
        plantList = plantList :+ p
        loadingPlantView.incrementProgressIndicator(
          plantList.size * progressIndicatorIncrement(plantSelectorModel.getPlantSelectedCount)
        )
        Continue
      }
      private val onCompletePlantsEmission: () => Unit = () => loadingPlantView.setNewScene()

      override def setupBehaviour(): Unit =
        plantSelectorModel.registerCallbackPlantInfo(
          onNextPlantEmitted,
          (ex: Throwable) => ex.printStackTrace(),
          onCompletePlantsEmission
        )

      override def beforeNextScene(): Unit =
        simulationMVC.simulationController.plantsSelected = plantList.toList
        val environmentMVC = component.EnvironmentMVC(simulationMVC)
        loadingPlantView.moveToNextScene(environmentMVC.environmentView)

  trait Interface extends Provider with Component:
    self: Requirements =>
