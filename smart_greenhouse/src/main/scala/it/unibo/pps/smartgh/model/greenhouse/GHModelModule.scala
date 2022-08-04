package it.unibo.pps.smartgh.model.greenhouse

/** Implementation of the [[GHModelModule]]. */
object GHModelModule:
  /** This trait exposes the methods for managing the GreenHouse model. */
  trait Model:
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

    /** Plants of the greenhouse.
     * @return
     *   the list of plants
     */
    val plants: List[String]

  /** A trait for defining the model instance.*/
  trait Provider:
    val model: Model

  /** A trait that represents the greenhouse model component. */
  trait Component:
    /** Implementation of the greenhouse model.*/
    class GreenHouseImpl(override val plants: List[String], override val areas: List[String], override val city: String)
      extends Model:
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
        
  /** Trait that combine provider and component for greenhouse model.*/
  trait Interface extends Provider with Component