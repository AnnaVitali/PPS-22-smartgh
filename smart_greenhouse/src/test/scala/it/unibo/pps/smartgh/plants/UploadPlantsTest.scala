package it.unibo.pps.smartgh.plants

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io.FileNotFoundException

class UploadPlantsTest extends AnyFunSuite with Matchers:
  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "plants.csv"
  private val prologFile = "plants.pl"
  private val uploader = UploadPlants

  test(s"$file file lines number should equal to 25") {
    uploader.countResourcesFileLines(file) shouldEqual 25
  }

  test(s"$prologFile should have the same number of lines of $file") {
    uploader.writePrologFile(path, file, prologFile)
    uploader.countPrologFileLines(path + prologFile) shouldEqual uploader.countResourcesFileLines(file)
  }

  test("passing a file that does not exist should yield a FileNotFoundException") {
    assertThrows[FileNotFoundException](uploader.getBufferedSource("noFile"))
  }