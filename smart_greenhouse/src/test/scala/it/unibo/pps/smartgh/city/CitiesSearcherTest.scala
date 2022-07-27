package it.unibo.pps.smartgh.city

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers as ShouldMatchers
import org.scalatest.matchers.must.Matchers as MustMatchers

/** This class contains the tests to verify that the [[CitiesSearcher]] work correctly. */
class CitiesSearcherTest extends AnyFunSuite:

  private val citiesSearcher = CitiesSearcher()

  test("Cities Searcher should show cities") {
    import ShouldMatchers._
    citiesSearcher.getAllCities.size should be > 0
  }

  test("Cities Searcher should correctly return a specific searched city") {
    import ShouldMatchers._
    val city = "Cesena"
    citiesSearcher.searchCities(city) should contain(city)
  }

  test("Cities Searcher should correctly return searched cities starting with the specific chars") {
    import MustMatchers._
    val startWithChars = "F"
    citiesSearcher.searchCities(startWithChars).foreach(c => c must startWith(startWithChars))
  }
