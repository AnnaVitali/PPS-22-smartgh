package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import javafx.scene.layout.GridPane

/** A trait that represents the MVC component for area parameters. */
trait AreaParametersMVC:

  /** The area model component. */
  val areaModel: AreaModel

  /** The parameter view component. */
  val parameterView: AreaParametersView

  /** The parameter controller component. */
  val parameterController: AreaParametersController
