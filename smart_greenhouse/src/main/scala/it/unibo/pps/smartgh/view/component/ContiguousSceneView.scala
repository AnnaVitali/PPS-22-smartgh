package it.unibo.pps.smartgh.view.component

import javafx.scene.Parent

/** Trait useful to define a view component that has to set a new contiguous scene. */
trait ContiguousSceneView[A <: Parent]:

  /** Method to ask the controller to instantiate the new MVC for next scene, specifying the base view. */
  def setNewScene(): Unit

  /** Method to specify the main view to display a new view component.
    * @param component
    *   view component to display in the next scene
    */
  def moveToNextScene(component: ViewComponent[A]): Unit
