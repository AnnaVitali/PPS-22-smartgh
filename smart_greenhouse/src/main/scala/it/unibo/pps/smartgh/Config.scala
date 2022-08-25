package it.unibo.pps.smartgh

/** Configuration object. */
object Config:
  /** Path to which prolog files are stored. */
  val Path: String = System.getProperty("user.home") + "/pps/"
  /** Cities input file name. */
  val CitiesInputFile: String = "cities.txt"
  /** Plants input file name. */
  val PlantsInputFile: String = "plants.txt"
  /** Cities output file name. */
  val CitiesOutputFile: String = "cities.pl"
  /** Plants output file name. */
  val PlantsOutputFile: String = "plants.pl"
