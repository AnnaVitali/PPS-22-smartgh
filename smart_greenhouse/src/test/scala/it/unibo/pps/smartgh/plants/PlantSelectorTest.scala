package it.unibo.pps.smartgh.plants

import alice.tuprolog.Theory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests realized to verify that {@link PlantSelector} behaves correctly */
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
    plantSelector.getPlantsSelectedName(plantIndex) should equal(selectedPlant)
  }

  test(s"$PS should mantain the selected plants identifier") {
    val plantIndex = 0
    val selectedPlant = plantSelector.getAllAvailablePlants(plantIndex)
    plantSelector.selectPlant(selectedPlant)
    plantSelector.getPlantsSelectedIdentifier.size should equal(plantSelector.getPlantsSelectedName.size)
  }
