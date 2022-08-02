package it.unibo.pps.smartgh.model.city

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}

import java.io.FileNotFoundException
import scala.io.Source
/** This class contains the tests to verify that the [[UploadCities]] works correctly. */
class UploadCitiesTest extends AnyFunSuite with Matchers:
  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "cities.txt"
  private val prologFile = "cities.pl"
  private val uploader = UploadCities

  test(s"$file file lines number should equal to 7978") {
    uploader.countResourcesFileLines(file) shouldEqual 7978
  }

  test(s"$prologFile should have the same number of lines of $file + 1") {
    uploader.writePrologFile(path, file, prologFile)
    uploader.countPrologFileLines(path + prologFile) shouldEqual uploader.countResourcesFileLines(file) + 1
  }

  test("passing a file that does not exist should yield a FileNotFoundException") {
    assertThrows[FileNotFoundException](uploader.getBufferedSource("noFile"))
  }
