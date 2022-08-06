package it.unibo.pps.smartgh.model.sensor

trait TimerEvent:
  def description: String
  def description_=(description: String): Unit

object TimerEvent:

  def apply(): TimerEvent = new TimerEventImpl()

  private class TimerEventImpl() extends TimerEvent:
    override var description: String = _
