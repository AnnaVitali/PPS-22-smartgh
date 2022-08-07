package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers as ShouldMatchers
import org.scalatest.matchers.must.Matchers as MustMatchers

/** This class contains the tests to verify that the [[SelectCityModelModule]] work correctly. */
class SelectCityModelModuleTest extends AnyFunSuite with SelectCityModelModule.Interface:

  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "cities.txt"
  private val prologFile = "cities.pl"
  UploadCities.writePrologFile(path, file, prologFile)

  override val selectCityModel: SelectCityModel = SelectCityModelImpl(path + prologFile)

  test("Cities Searcher should show cities") {
    import ShouldMatchers._
    selectCityModel.getAllCities.size should be > 0
  }

  test("Cities Searcher should correctly return a specific searched city") {
    import ShouldMatchers._
    val city = "Cesena"
    selectCityModel.searchCities(city) should contain(city)
  }

  test("Cities Searcher should correctly return searched cities starting with the specific chars") {
    import MustMatchers._
    val startWithChars = "F"
    selectCityModel.searchCities(startWithChars).foreach(c => c must startWith(startWithChars))
  }
