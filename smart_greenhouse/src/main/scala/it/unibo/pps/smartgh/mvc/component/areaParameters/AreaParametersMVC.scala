package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import javafx.scene.layout.GridPane

trait AreaParametersMVC:
  val areaModel: AreaModel
  val parameterView: AreaParametersView
  val parameterController: AreaParametersController
