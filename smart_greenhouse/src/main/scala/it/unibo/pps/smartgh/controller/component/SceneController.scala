package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.view.component.BaseView

trait SceneController:

  /** Method for the controller to doing something before next scene. */
  def beforeNextScene(): Unit
