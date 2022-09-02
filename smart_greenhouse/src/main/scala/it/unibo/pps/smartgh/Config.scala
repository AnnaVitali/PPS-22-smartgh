package it.unibo.pps.smartgh

import java.io.File

/** Configuration object. */
object Config:
  /** Path to which prolog files are stored. */
  val Path: String = System.getProperty("user.home") + File.separator + "pps" + File.separator
  /** Cities input file name. */
  val CitiesInputFile: String = "cities.txt"
  /** Plants input file name. */
  val PlantsInputFile: String = "plants.txt"
  /** Cities output file name. */
  val CitiesOutputFile: String = "cities.pl"
  /** Plants output file name. */
  val PlantsOutputFile: String = "plants.pl"
