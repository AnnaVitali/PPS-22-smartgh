package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests to verify that the [[SelectCityModelModule]] work correctly. */
class SelectCityModelModuleTest extends AnyFunSuite with SelectCityModelModule.Interface with Matchers:

  private val path = Config.Path
  private val file = Config.CitiesInputFile
  private val prologFile = Config.CitiesOutputFile
  UploadCities.writePrologFile(path, file, prologFile)

  override val selectCityModel: SelectCityModel = SelectCityModelImpl(path + prologFile)

  test("Select city should correctly return a specific searched city") {
    val city = "Rome"
    selectCityModel.searchCities(city) should contain(city)
  }

  test("Select city should correctly return searched cities starting with the specific chars") {
    val startWithChars = "F"
    selectCityModel.searchCities(startWithChars).foreach(_ should startWith(startWithChars))
  }

  test("Select wrong city should return None when get city info") {
    val city = "Wrong city"
    selectCityModel.getCityInfo(city) shouldEqual None
  }

  test("Select city should return city information when get city info") {
    val cityInfo = ("Rome", "41.8931", "12.4828")
    selectCityModel.getCityInfo(cityInfo._1) shouldEqual Some(cityInfo)
  }
