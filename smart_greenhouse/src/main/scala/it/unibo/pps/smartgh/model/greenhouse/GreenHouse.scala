package it.unibo.pps.smartgh.model.greenhouse
/** This trait exposes the methods for managing the GreenHouse model */
trait GreenHouse:
  /** Division of the greenhouse.
    * @return
    *   tuple indicating the number of rows and cols (r, c).
    */
  val dimension: (Int, Int)

  /** Areas of the greenhouse.
    * @return
    *   the list of areas
    */
  val areas: List[String]

  /** City where the greenhouse is placed. */
  val city: String //TODO change String to City

/** Implementation of the [[GreenHouse]]. */
object GreenHouse:
  /** Apply method for the [[GreenHouse]].
    * @return
    *   the [[GreenHouse]] object.
    */
  def apply(plants: List[String], areas: List[String], city: String): GreenHouseImpl =
    GreenHouseImpl(plants, areas, city)

  class GreenHouseImpl(val plants: List[String], override val areas: List[String], override val city: String)
      extends GreenHouse:

    override val dimension: (Int, Int) =
      val factors = for
        i <- 1 to Math.sqrt(plants.length).toInt
        if plants.length % i == 0
      yield (i, plants.length / i)
      //print(factors)
      factors.foldLeft(factors.headOption)((f, acc) =>
        f.fold(Some(0,0))((r, c) => 
          if Math.abs(r - c) <= Math.abs(acc._1 - acc._2) then Some(r, c)
          else Some(acc._1, acc._2)
        )
      ).getOrElse((0,0))
