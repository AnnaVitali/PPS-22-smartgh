package it.unibo.pps.smartgh.model.area

import scala.util.Random

/** State of the gates. */
enum AreaGatesState:
  case Open, Close
export AreaGatesState.*

/** State of the shields. */
enum AreaShieldState:
  case Up, Down
export AreaShieldState.*

/** State of the atomise system. */
enum AreaAtomiseState:
  case AtomisingActive, AtomisingInactive
export AreaAtomiseState.*

/** State of the ventilation system. */
enum AreaVentilationState:
  case VentilationActive, VentilationInactive
export AreaVentilationState.*

/** State of the soil moisture system. */
enum AreaHumidityState:
  case Watering, MovingSoil, None
export AreaHumidityState.*

/** Object that monitors the state of the area components, in relation to the actions performed by the user for changing
  * the value detected by the sensors.
  */
object AreaComponentsState:

  private val InitialLampBrightness = 3000.0
  private val InitialTemperature = 27.0
  private val MaxInitialHumidity = 30.0
  private val MinInitialHumidity = 10.0

  /** Apply method for the [[AreaComponentsStateImpl]].
    * @return
    *   the [[AreaComponentsStateImpl]] representing the area components state.
    */
  def apply(): AreaComponentsStateImpl = AreaComponentsStateImpl()

  /** Implementation of the [[AreaComponentsStateImpl]]. */
  class AreaComponentsStateImpl:
    var gatesState: AreaGatesState = AreaGatesState.Open
    var shieldState: AreaShieldState = AreaShieldState.Up
    var atomisingState: AreaAtomiseState = AreaAtomiseState.AtomisingInactive
    var ventilationState: AreaVentilationState = AreaVentilationState.VentilationInactive
    var brightnessOfTheLamps: Double = InitialLampBrightness
    var temperature: Double = InitialTemperature
    val soilHumidity: Double = Random.nextDouble() * (MaxInitialHumidity - MinInitialHumidity) + MinInitialHumidity
    var humidityActions: AreaHumidityState = AreaHumidityState.None

    def copy(): AreaComponentsStateImpl =
      val copy = AreaComponentsState()
      copy.shieldState = shieldState
      copy.temperature = temperature
      copy.gatesState = gatesState
      copy.humidityActions = humidityActions
      copy.atomisingState = atomisingState
      copy.ventilationState = ventilationState
      copy.brightnessOfTheLamps = brightnessOfTheLamps
      copy
