package it.unibo.pps.smartgh.city

import java.io.*
import java.nio.file.{Files, Path}
import scala.io.{BufferedSource, Codec, Source}
import scala.language.postfixOps
import scala.util.Using

trait UploadCities:

  /** function that get the file
    *
    * @throws java.io.FileNotFoundException
    * @param fileName
    *   name of file
    * @return
    *   BufferdSource
    */
  def getBufferedSource(fileName: String): BufferedSource

  /** function that get the number of lines of a file located in resources folder
    *
    * @throws java.io.FileNotFoundException
    * @param fileName
    *   name of file
    * @return
    *   number of lines
    */
  def countResourcesFileLines(fileName: String): Int

  /** function that get the number of lines of a file located in prolog folder
    *
    * @throws java.io.FileNotFoundException
    * @param fileName
    *   name of file
    * @return
    *   number of lines
    */
  def countPrologFileLines(fileName: String): Int

  /** function that write the content of the input file into the output file formatted with the following prolog Theory:
    * citta("cityName"). ricerca_citta([H|T], X) :- citta(X), atom_chars(X, [H|T]).
    *
    * @param path
    *   the path to save the file
    * @param inputFile
    *   input file that contains cities names
    * @param outFile
    *   prolog output file with the cities theories
    * @return
    *   number of lines
    */
  def writePrologFile(path: String, inputFile: String, outFile: String): Unit

/** Utility for reading cities.txt form a file and create a prolog file containing cities.txt */
object UploadCities extends UploadCities:

  def getBufferedSource(fileName: String): BufferedSource =
    Source.fromResource(fileName)(Codec.UTF8)

  def countResourcesFileLines(fileName: String): Int =
    Using(getBufferedSource(fileName)) {
      _.getLines().length
    }.get

  def countPrologFileLines(path: String): Int =
    Using(Source.fromFile(path)) {
      _.getLines().length
    }.get

  import java.text.Normalizer

  def writePrologFile(path: String, inputFile: String, outFile: String): Unit =
    val dir = new File(path)
    if !dir.exists() then Files.createDirectory(Path.of(path))

    val file = new File(path, outFile)

    val writer = new FileWriter(file)
    val printWriter = new PrintWriter(writer)
    try {
      Using(getBufferedSource(inputFile)) {
        _.getLines() foreach (c => printWriter.println(s"citta(\"$c\")."))
      }
      printWriter.println(s"ricerca_citta([H|T], X) :- citta(X), atom_chars(X, [H|T]).")
    } finally printWriter.close()
