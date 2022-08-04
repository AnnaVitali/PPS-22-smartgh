package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.AreaControllerModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.AreaMVC.AreaMVCImpl
import it.unibo.pps.smartgh.view.component.AreaViewModule
import it.unibo.pps.smartgh.view.component.AreaViewModule.Component

/** Implementation of the [[GHModelModule]]. */
object GHModelModule:
  /** This trait exposes the methods for managing the GreenHouse model. */
  trait GreenHouseModel:
    /** Division of the greenhouse.
     * @return
     *   tuple indicating the number of rows and cols (r, c).
     */
    val dimension: (Int, Int)

    /** Areas of the greenhouse.
     * @return
     *   the list of areas
     */
    var areas: List[AreaMVCImpl]

    /** Plants of the greenhouse.
     * @return
     *   the list of plants
     */
    val plants: List[Plant]

  /** A trait for defining the model instance.*/
  trait Provider:
    val ghDivisionModel: GreenHouseModel

  /** A trait that represents the greenhouse model component. */
  trait Component:
    /** Implementation of the greenhouse model.*/
    class GreenHouseImpl(override val plants: List[Plant])
      extends GreenHouseModel:

      override var areas: List[AreaMVCImpl] = List.empty

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