package it.unibo.pps.smartgh.controller.component

/** A trait for the controllers to manage scenes. */
trait SceneController:

  /** Method for the controller to do something before next scene. */
  def beforeNextScene(): Unit
