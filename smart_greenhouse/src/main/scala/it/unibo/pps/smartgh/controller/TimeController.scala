package it.unibo.pps.smartgh.controller

trait TimeController:
  def startSimulationTimer(): Unit
  def stopSimulationTimer(): Unit
  def updateVelocityTimer(speed: Double): Unit

object TimeController:

  def apply(): TimeController = TimeControllerImpl()

  private class TimeControllerImpl extends TimeController:

    override def startSimulationTimer(): Unit = ??? //TODO
    override def stopSimulationTimer(): Unit = ??? //TODO
    override def updateVelocityTimer(speed: Double): Unit = ??? //TODO
