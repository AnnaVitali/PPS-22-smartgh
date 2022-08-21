package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers as MustMatchers
import org.scalatest.matchers.should.Matchers as ShouldMatchers

/** This class contains the tests to verify that the [[SelectCityModelModule]] work correctly. */
class SelectCityModelModuleTest extends AnyFunSuite with SelectCityModelModule.Interface:

  private val path = Config.path
  private val file = Config.citiesInputFile
  private val prologFile = Config.citiesOutputFile
  UploadCities.writePrologFile(path, file, prologFile)

  override val selectCityModel: SelectCityModel = SelectCityModelImpl(path + prologFile)

  test("Cities Searcher should show cities") {
    import ShouldMatchers.*
    selectCityModel.getAllCities.size should be > 0
  }

  test("Cities Searcher should correctly return a specific searched city") {
    import ShouldMatchers.*
    val city = "Cesena"
    selectCityModel.searchCities(city) should contain(city)
  }

  test("Cities Searcher should correctly return searched cities starting with the specific chars") {
    import MustMatchers.*
    val startWithChars = "F"
    selectCityModel.searchCities(startWithChars).foreach(c => c must startWith(startWithChars))
  }
