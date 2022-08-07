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

object AreaComponentsState:

  def apply(): AreaComponentsStateImpl = AreaComponentsStateImpl()

  class AreaComponentsStateImpl:
    val defaultValueLampsBrightness = 100
    var gatesState: AreaGatesState = AreaGatesState.Close
    var shieldState: AreaShieldState = AreaShieldState.Up
    var atomisingState: AreaAtomiseState = AreaAtomiseState.AtomisingInactive
    var ventilationState: AreaVentilationState = AreaVentilationState.VentilationInactive
    var brightnessOfTheLamps: Int = defaultValueLampsBrightness
    var temperature: Double = _
    var humidityActions: AreaHumidityState = AreaHumidityState.None
