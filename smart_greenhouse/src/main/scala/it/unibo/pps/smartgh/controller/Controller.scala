package it.unibo.pps.smartgh.controller

trait Controller:
  def startSimulationTimer(): Unit

object Controller:

  def apply(): Controller = ControllerImpl()

  private class ControllerImpl extends Controller:

    override def startSimulationTimer(): Unit = ??? //TODO
