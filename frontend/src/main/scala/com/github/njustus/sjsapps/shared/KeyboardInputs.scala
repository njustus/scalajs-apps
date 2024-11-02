package com.github.njustus.sjsapps.shared

import japgolly.scalajs.react.facade.SyntheticKeyboardEvent
import org.scalajs.dom.console

enum KeyboardInputs(val keyCodes: Int*) {
  case Up    extends KeyboardInputs(38, 87)
  case Down  extends KeyboardInputs(40, 83)
  case Left  extends KeyboardInputs(37, 65)
  case Right extends KeyboardInputs(39, 68)

  def delta: Coordinate = this match {
    case KeyboardInputs.Up    => Coordinate(0, -1)
    case KeyboardInputs.Down  => Coordinate(0, 1)
    case KeyboardInputs.Left  => Coordinate(-1, 0)
    case KeyboardInputs.Right => Coordinate(1, 0)
  }
}

object KeyboardInputs {

  def fromKeyCode(keyCode: Int): Option[KeyboardInputs] =
    KeyboardInputs.values.find { kInput =>
      kInput.keyCodes.contains(keyCode)
    }

  def fromKeyBoardEvent(ev: SyntheticKeyboardEvent[?]): Option[KeyboardInputs] =
    fromKeyCode(ev.keyCode)
}
