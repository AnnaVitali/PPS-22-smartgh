package it.unibo.pps.smartgh.controller

trait Controller:
  def startSimulationTimer(): Unit
  def stopSimulationTimer(): Unit
  def updateVelocityTimer(speed: Double): Unit

object Controller:

  def apply(): Controller = ControllerImpl()

  private class ControllerImpl extends Controller:

    override def startSimulationTimer(): Unit = ??? //TODO
    override def stopSimulationTimer(): Unit = ??? //TODO
    override def updateVelocityTimer(speed: Double): Unit = ??? //TODO
