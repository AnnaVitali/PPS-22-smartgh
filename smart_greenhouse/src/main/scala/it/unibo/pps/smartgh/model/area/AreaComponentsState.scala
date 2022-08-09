package it.unibo.pps.smartgh.model.sensor.areaComponentsState

enum AreaGatesState:
  case Open, Close
export AreaGatesState.*

enum AreaShieldState:
  case Up, Down
export AreaShieldState.*

enum AreaAtomiseState:
  case AtomisingActive, AtomisingInactive
export AreaAtomiseState.*

enum AreaVentilationState:
  case VentilationActive, VentilationInactive
export AreaVentilationState.*

enum AreaHumidityState:
  case Watering, MovingSoil, None
export AreaHumidityState.*

/** Object that monitoring the state of the area components in relation to the actions performed by the user for
  * changing the value detected by the sensors.
  */
object AreaComponentsState:

  /** Apply method for the [[AreaComponentsStateImpl]].
    * @return
    *   the [[AreaComponentsStateImpl]] representing the area components state.
    */
  def apply(): AreaComponentsStateImpl = AreaComponentsStateImpl()

  class AreaComponentsStateImpl:
    var gatesState: AreaGatesState = AreaGatesState.Close
    var shieldState: AreaShieldState = AreaShieldState.Up
    var atomisingState: AreaAtomiseState = AreaAtomiseState.AtomisingInactive
    var ventilationState: AreaVentilationState = AreaVentilationState.VentilationInactive
    var brightnessOfTheLamps: Double = 100.0
    var temperature: Double = 27.0
    var soilHumidity: Double = 30.0
    var humidityActions: AreaHumidityState = AreaHumidityState.None
