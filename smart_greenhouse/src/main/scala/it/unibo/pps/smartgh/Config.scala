package it.unibo.pps.smartgh

/** Configuration object. */
object Config:
  /** Path to which prolog files are stored. */
  val path: String = System.getProperty("user.home") + "/pps/"
  /** Cities input file name. */
  val citiesInputFile: String = "cities.txt"
  /** Plants input file name. */
  val plantsInputFile: String = "plants.txt"
  /** Cities output file name. */
  val citiesOutputFile: String = "cities.pl"
  /** Plants output file name. */
  val plantsOutputFile: String = "plants.pl"