package it.unibo.pps.smartgh.plants

import alice.tuprolog.Theory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.util.NoSuchElementException

/** This class contains the tests realized to verify that [[PlantSelector]] behaves correctly. */
class PlantSelectorTest extends AnyFunSuite with Matchers:

  private val PS = "Plant Selector"
  private val plantSelector = PlantSelector.apply()

  test(s"$PS should show all the possibile plants that can be cultivated") {
    plantSelector.getAllAvailablePlants.size should be > 0
  }

  test(s"$PS should mantain the selected plants name") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.getPlantsSelectedName(plantIndex) shouldEqual selectedPlant
  }

  test(s"$PS should allow the deselection of a plant") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.deselectPlant(selectedPlant)
    plantSelector.getPlantsSelectedName.size shouldEqual 0
  }

  test(
    "Calling the deselectPlant function with a plant that hasn't been selected should yield " +
      "NoSuchElementException"
  ) {
    val plantIndex = 0
    val notSelectedPlant = plantSelector.getAllAvailablePlants(plantIndex)
    assertThrows[NoSuchElementException] {
      plantSelector.deselectPlant(notSelectedPlant)
    }
  }

  test(s"$PS should mantain the selected plants identifier") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.getPlantsSelectedIdentifier.size shouldEqual plantSelector.getPlantsSelectedName.size
  }
