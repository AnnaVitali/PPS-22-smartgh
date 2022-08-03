package it.unibo.pps.smartgh.model.plants

import org.scalatest.BeforeAndAfter
import alice.tuprolog.Theory
import it.unibo.pps.smartgh.model.plants.PlantSelectorModel.Model
import it.unibo.pps.smartgh.model.plants.PlantSelectorModel.PlantSelectorComponent
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.util.NoSuchElementException

/** This class contains the tests realized to verify that [[Model]] behaves correctly. */
class ModelModelTest
    extends AnyFunSuite
    with Matchers
    with BeforeAndAfter
    with PlantSelectorModel.PlantSelectorInterface:

  private val PS = "Plant Selector"
  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "plants.txt"
  private val prologFile = "plants.pl"
  private val uploader = UploadPlants
  override var model: Model = _

  before {
    uploader.writePrologFile(path, file, prologFile)
    model = new PlantSelectorModelImpl(path + prologFile)
  }

  test(s"$PS should show all the possibile plants that can be cultivated") {
    plantSelector.getAllAvailablePlants().size should be > 0
  }

  test(s"$PS should mantain the selected plants name") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants()(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.getPlantsSelectedName()(plantIndex) shouldEqual selectedPlant
  }

  test(s"$PS should allow the deselection of a plant") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants()(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.deselectPlant(selectedPlant)
    plantSelector.getPlantsSelectedName().size shouldEqual 0
  }

  test(
    "Calling the deselectPlant function with a plant that hasn't been selected should yield " +
      "NoSuchElementException"
  ) {
    val plantIndex = 0
    val notSelectedPlant = plantSelector.getAllAvailablePlants()(plantIndex)
    assertThrows[NoSuchElementException] {
      plantSelector.deselectPlant(notSelectedPlant)
    }
  }

  test(s"$PS should mantain the selected plants identifier") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants()(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.getPlantsSelectedIdentifier().size shouldEqual plantSelector.getPlantsSelectedName().size
  }
