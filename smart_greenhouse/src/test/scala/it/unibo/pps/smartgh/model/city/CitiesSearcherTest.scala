package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.model.city.CitySearcherModelModule.CitySearcherModel
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers as ShouldMatchers
import org.scalatest.matchers.must.Matchers as MustMatchers

/** This class contains the tests to verify that the [[CitiesSearcher]] work correctly. */
class CitiesSearcherTest extends AnyFunSuite with BeforeAndAfter with CitySearcherModelModule.Interface:

  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "cities.txt"
  private val prologFile = "cities.pl"
  override val citySearcherModel: CitySearcherModel = CitySearcherModelImpl(path + prologFile)

  before {
    UploadCities.writePrologFile(path, file, prologFile)
  }

  test("Cities Searcher should show cities") {
    import ShouldMatchers._
    citySearcherModel.getAllCities.size should be > 0
  }

  test("Cities Searcher should correctly return a specific searched city") {
    import ShouldMatchers._
    val city = "Cesena"
    citySearcherModel.searchCities(city) should contain(city)
  }

  test("Cities Searcher should correctly return searched cities starting with the specific chars") {
    import MustMatchers._
    val startWithChars = "F"
    citySearcherModel.searchCities(startWithChars).foreach(c => c must startWith(startWithChars))
  }
