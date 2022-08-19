package it.unibo.pps.smartgh.view.component.areaParameters

object AreaParametersView:

  trait AreaParametersView:
    def updateCurrentValue(value: Double, status: String): Unit

  abstract class AbstractAreaParametersView extends AreaParametersView
