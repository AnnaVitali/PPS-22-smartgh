package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

/** A basic view component.
  *
  * This trait represents a modular view components that encapsulates all the common logic required to create and model
  * the view components.
  *
  * @tparam A
  *   the type of component that is wrapped in the FX layout
  */
trait ViewComponent[A <: Parent]:

  /** Return the component that is wrapped by the [[ViewComponent]] */
  val component: A

/** Object that can be used to create new instances of [[ViewComponent]]. */
object ViewComponent:

  /** Convert the object to its wrapped component */
  given toComponent[A <: Parent]: Conversion[ViewComponent[A], A] with
    override def apply(component: ViewComponent[A]): A = component.component

  /** An abstract class that can instantiated the FX components from FXML files.
    *
    * This class set the [[FXMLLoader]] object, the subclasses need to load the file through the loader with its
    * specific component type.
    * @param fxmlFileName
    *   the name of the fxml file that is situated in /resources/fxml/ directory
    * @tparam A
    *   the type of the FX component to wrap
    */
  abstract class AbstractViewComponent[A <: Parent](fxmlFileName: String) extends ViewComponent[A]:
    protected val loader: FXMLLoader = FXMLLoader()
    loader.setController(this)
    loader.setLocation(getClass.getResource("/fxml/" + fxmlFileName))
