package it.unibo.pps.smartgh.model.plants

import alice.tuprolog.Theory
import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.{Component, PlantSelectorModel}
import monix.execution.Ack.{Continue, Stop}
import monix.execution.Cancelable
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.util.NoSuchElementException

/** This class contains the tests realized to verify that [[PlantSelectorModel]] behaves correctly. */
class PlantSelectorModelModuleTest
    extends AnyFunSuite
    with Matchers
    with BeforeAndAfter
    with PlantSelectorModelModule.Interface:

  private val PS = "Plant Selector"
  private val path = Config.Path
  private val file = Config.PlantsInputFile
  private val prologFile = Config.PlantsOutputFile
  private val uploader = UploadPlants
  uploader.writePrologFile(path, file, prologFile)
  override val plantSelectorModel = new PlantSelectorModelImpl(path + prologFile)
  private var plantsNameList: List[String] = _
  private var subscription: Option[Cancelable] = None

  after {
    if subscription.isDefined then subscription.getOrElse(cancel())
  }

  test(s"$PS should show all the possible plants that can be cultivated") {
    plantsNameList = plantSelectorModel.getAllAvailablePlants
    plantsNameList.size should be > 0
  }

  test(s"$PS should maintain the selected plants name") {
    val plantIndex = 0
    val selectedPlant = plantsNameList(plantIndex)
    val onNext = (plantList: List[String]) => {
      plantList(plantIndex) shouldEqual selectedPlant
      Continue
    }
    subscription =
      Some(plantSelectorModel.registerCallbackPlantSelection(onNext, (ex: Throwable) => ex.printStackTrace(), () => {}))
    plantSelectorModel.selectPlant(selectedPlant)
  }

  test(s"$PS should maintain the selected plants identifier") {
    val plantIndex = 0
    val selectedPlant = plantsNameList(plantIndex)
    val onNext = (plantList: List[String]) => {
      plantList.size shouldEqual plantSelectorModel.getPlantsSelectedName.size
      plantList.size shouldEqual plantSelectorModel.getPlantsSelectedIdentifier.size
      Continue
    }
    plantSelectorModel.selectPlant(selectedPlant)
    subscription = Some(
      plantSelectorModel.registerCallbackPlantSelection(onNext, (ex: Throwable) => ex.printStackTrace(), () => {})
    )

  }

  test(s"$PS should maintain the selected plants") {
    val plantIndex = 0
    val selectedPlant = plantsNameList(plantIndex)
    var plantList: Vector[Plant] = Vector()
    val onNextPlant = (p: Plant) => {
      plantList = plantList :+ p
      Continue
    }
    val onCompletePlantList: () => Unit = () => plantList shouldEqual plantSelectorModel.getPlantsSelectedName.size
    plantSelectorModel.selectPlant(selectedPlant)
    subscription = Some(
      plantSelectorModel.registerCallbackPlantInfo(
        onNextPlant,
        (ex: Throwable) => ex.printStackTrace(),
        onCompletePlantList
      )
    )
  }

  test(s"$PS should allow the deselection of a plant") {
    val plantIndex = 0
    val selectedPlant = plantsNameList(plantIndex)
    var operationNumber = 1
    val onNext = (plantList: List[String]) => {
      if operationNumber == 1 then
        plantList.size shouldEqual plantSelectorModel.getPlantsSelectedName.size
        plantList.size shouldEqual plantSelectorModel.getPlantsSelectedIdentifier.size
        operationNumber = operationNumber + 1
      else assert(plantList.isEmpty)
      Continue
    }
    subscription =
      Some(plantSelectorModel.registerCallbackPlantSelection(onNext, (ex: Throwable) => ex.printStackTrace(), () => {}))
    plantSelectorModel.selectPlant(selectedPlant)
    plantSelectorModel.deselectPlant(selectedPlant)
  }

  test(
    "Calling the deselectPlant function with a plant that hasn't been selected should yield " +
      "NoSuchElementException wit the following error message: You can't deselect a plant that hasn't been selected!"
  ) {
    val plantIndex = 0
    val notSelectedPlant = plantsNameList(plantIndex)
    val errorMessage = "You can't deselect a plant that hasn't been selected!"
    val onError: Throwable => Unit = (ex: Throwable) => {
      assert(ex.isInstanceOf[NoSuchElementException])
      assert(ex.getMessage === errorMessage)
    }
    subscription = Some(plantSelectorModel.registerCallbackPlantSelection(_ => Continue, onError, () => {}))
    plantSelectorModel.deselectPlant(notSelectedPlant)
  }
