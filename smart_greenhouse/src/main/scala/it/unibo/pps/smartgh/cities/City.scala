package it.unibo.pps.smartgh.cities

import scala.annotation.targetName



trait City:
  /** datastructure that will contains the city's environment values*/
  type EnvironmentValues

  /** @return city's name*/
  def name: String
  /**Set the city's name
   * @param city's name */
  def name_: (n: String): Unit

  /** @return city's environment values*/
  def environmentValues: EnvironmentValues

