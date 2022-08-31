package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.mvc.component.AreaMVC

/** Implementation of the [[GHModelModule]]. */
object GHModelModule:
  /** This trait exposes the methods for managing the GreenHouse model. */
  trait GreenHouseModel:

    /** Areas of the greenhouse.
      * @return
      *   the list of areas
      */
    var areas: List[AreaMVC]

  /** A trait for defining the model instance. */
  trait Provider:
    /** The green house division model. */
    val ghDivisionModel: GreenHouseModel

  /** A trait that represents the greenhouse model component. */
  trait Component:
    /** Implementation of the greenhouse model. */
    class GreenHouseDivisionModelImpl() extends GreenHouseModel:

      override var areas: List[AreaMVC] = List.empty

  /** Trait that combine provider and component for greenhouse model. */
  trait Interface extends Provider with Component
