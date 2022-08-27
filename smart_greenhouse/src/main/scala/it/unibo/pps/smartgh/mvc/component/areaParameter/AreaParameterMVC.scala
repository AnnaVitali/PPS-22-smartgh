package it.unibo.pps.smartgh.mvc.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.AreaParameterController
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.AreaParameterView
import javafx.scene.layout.GridPane

/** A trait that represents the MVC component for area parameters. */
trait AreaParameterMVC:

  /** The area model component. */
  val areaModel: AreaModel

  /** The parameter view component. */
  val parameterView: AreaParameterView

  /** The parameter controller component. */
  val parameterController: AreaParameterController
