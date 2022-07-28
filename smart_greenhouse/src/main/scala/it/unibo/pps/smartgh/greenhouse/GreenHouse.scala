package it.unibo.pps.smartgh.greenhouse

trait GreenHouse:
  /**
   * division of the greenhouse
   * @return tuple indicating the number of rows and cols (r, c)
   * */
  val dimension: (Int, Int)

  /** areas of the greenhouse
   * @return the list of areas*/
  val areas: List[String]

  /**City where the greenhouse is placed */
  val city: String //TODO change String to City

/** Implementation of the [[GreenHouse]]*/
object GreenHouse:
  def apply(plants: List[String], areas: List[String], city: String): GreenHouseImpl = GreenHouseImpl(plants, areas, city)

  class GreenHouseImpl(val plants: List[String],
                       override val areas: List[String],
                       override val city: String) extends GreenHouse:

    override val dimension: (Int, Int) =
      val factors = for
        i <- 1 to Math.sqrt(plants.length).toInt
        if plants.length % i == 0
      yield
        (i, plants.length / i)
      //print(factors)
      factors.foldLeft(factors.head)((f, acc) =>
        if Math.abs(f._1 - f._2) <= Math.abs(acc._1 - acc._2) then
          (f._1, f._2)
        else
          (acc._1, acc._2)
      )