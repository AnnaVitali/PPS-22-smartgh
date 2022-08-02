package it.unibo.pps.smartgh.model.plants

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlantTest extends AnyFunSuite with Matchers:
  val plant: Plant = Plant("lemon", "citrus limon")

  test("after creating lemon plant when we ask its name should be equal to lemon") {
    plant.name shouldEqual "lemon"
  }

  test("after creating lemon plant when we ask its id should be equal to citrus limon") {
    plant.id shouldEqual "citrus limon"
  }

  test(
    "after creating lemon plant when we ask its image_url should be equal to https://opb-img.plantbook.io/citrus%20limon.jpg"
  ) {
    plant.imageUrl shouldEqual "https://opb-img.plantbook.io/citrus%20limon.jpg"
  }

  test("after creating lemon plant when we ask its optimal values should not be empty ") {
    plant.optimalValues should not equal Map.empty[String, Any]
  }

  test("after creating lemon plant when we ask its optimal values should be correct") {
    val optimalValues = Map(
      ("max_light_lux", 65000),
      ("min_light_lux", 4000),
      ("max_temp", 35),
      ("min_temp", 8),
      ("max_env_humid", 85),
      ("min_env_humid", 30),
      ("max_soil_moist", 60),
      ("min_soil_moist", 15)
    )
    plant.optimalValues shouldEqual optimalValues
  }

  test("after creating lemon plant when we ask its description should not be empty") {
    plant.description should not equal ""
  }

  test("after creating lemon plant when we ask its description should be correct") {
    val description =
      "The lemon (Citrus limon) is a species of small evergreen trees in the flowering plant family Rutaceae, " +
        "native to Asia, primarily Northeast India (Assam), Northern Myanmar or China." +
        "The tree's ellipsoidal yellow fruit is used for culinary and non-culinary purposes throughout the world, primarily for its juice, " +
        "which has both culinary and cleaning uses. The pulp and rind are also used in cooking and baking. " +
        "The juice of the lemon is about 5% to 6% citric acid, with a pH of around 2.2, giving it a sour taste. " +
        "The distinctive sour taste of lemon juice makes it a key ingredient in drinks and foods such as lemonade and lemon meringue pie."
    plant.description shouldEqual description
  }

  test("the icon of a plant that doesn't exist should be equal to default image") {
    val noPlant: Plant = Plant("", "")
    noPlant.imageUrl shouldEqual "images/plantIcon.png"
  }

  test("the description of a plant that doesn't exist should be equal to \"No description available\"") {
    val noPlant: Plant = Plant("", "")
    noPlant.description shouldEqual "No description available"
  }
