package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.component.AreaMVC.AreaMVCImpl
import it.unibo.pps.smartgh.view.component.AreaViewModule
import it.unibo.pps.smartgh.view.component.AreaViewModule.Component

/** Implementation of the [[GHModelModule]]. */
object GHModelModule:
  /** This trait exposes the methods for managing the GreenHouse model. */
  trait GreenHouseModel:

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

  /** A trait for defining the model instance. */
  trait Provider:
    /** The green house division model. */
    val ghDivisionModel: GreenHouseModel

  /** A trait that represents the greenhouse model component. */
  trait Component:
    /** Implementation of the greenhouse model.
      * @param plants
      *   list of [[Plant]]
      */
    class GreenHouseImpl(override val plants: List[Plant]) extends GreenHouseModel:

      override var areas: List[AreaMVCImpl] = List.empty

  /** Trait that combine provider and component for greenhouse model. */
  trait Interface extends Provider with Component
