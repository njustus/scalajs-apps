package com.github.njustus.sjsapps.snake

import japgolly.scalajs.react.facade.SyntheticKeyboardEvent

enum KeyboardInputs(val keyCodes: Int*) {
  case Up extends KeyboardInputs(38, 87)
  case Down extends KeyboardInputs(40, 83)
  case Left extends KeyboardInputs(39, 83)
  case Right extends KeyboardInputs(37, 68)

}

object KeyboardInputs {

  def fromKeyCode(keyCode: Int): Option[KeyboardInputs] =
    KeyboardInputs.values.find { kInput =>
      kInput.keyCodes.contains(keyCode)
    }

  def fromKeyBoardEvent(ev: SyntheticKeyboardEvent[_]): Option[KeyboardInputs] =
    fromKeyCode(ev.keyCode)
}
