package it.unibo.pps.smartgh.model.sensor.areaComponentsState

enum AreaGatesState:
  case Open, Close
export AreaGatesState.*

enum AreaShildState:
  case Up, Down
export AreaShildState.*

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
    private val defaultValueLampsBrightness = 100
    private var areaGatesState = AreaGatesState.Close
    private var areaShildState = AreaShildState.Up
    private var areaAtomiseState = AreaAtomiseState.AtomisingInactive
    private var areaVentilationState = AreaVentilationState.VentilationInactive
    private var lampsBrightness = defaultValueLampsBrightness
    private var temperatureSet: Double = _
    private var humidityState = AreaHumidityState.None

    def gatesState(): AreaGatesState = areaGatesState
    def gatesState_=(gatesState: AreaGatesState): Unit = areaGatesState = gatesState
    def shildState(): AreaShildState = areaShildState
    def shildState_=(shildState: AreaShildState): Unit = areaShildState = shildState
    def atomisingState(): AreaAtomiseState = areaAtomiseState
    def atomisingState_=(atomisingState: AreaAtomiseState): Unit = areaAtomiseState = atomisingState
    def ventilationState(): AreaVentilationState = areaVentilationState
    def ventilationState_=(ventilationState: AreaVentilationState): Unit = areaVentilationState = ventilationState
    def brightnessOfTheLamps(): Int = lampsBrightness
    def brightnessOfTheLamps_=(newLampsBrightness: Int): Unit = lampsBrightness = newLampsBrightness
    def temperature(): Double = temperatureSet
    def temperature_=(settedTemperature: Double): Unit = temperatureSet = settedTemperature
    def humidityActions(): AreaHumidityState = humidityState
    def humidityActions_=(value: AreaHumidityState): Unit = humidityState = value
