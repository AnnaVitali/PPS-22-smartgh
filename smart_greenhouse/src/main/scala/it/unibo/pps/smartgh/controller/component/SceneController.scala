package it.unibo.pps.smartgh.controller.component

trait SceneController:

  /** Method for the controller to doing something before next scene. */
  def beforeNextScene(): Unit
