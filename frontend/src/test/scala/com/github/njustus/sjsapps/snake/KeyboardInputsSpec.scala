package com.github.njustus.sjsapps.snake

import com.github.njustus.sjsapps.BaseTest
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

class KeyboardInputsSpec extends BaseTest {

    val VALID_KEYCODES = Seq(
      (38, KeyboardInputs.Up),
      (40, KeyboardInputs.Down),
      (37, KeyboardInputs.Left),
      (39, KeyboardInputs.Right),
    )

  "KeyboardInputs" should "be parsed by keyCodes" in {
    forAll(VALID_KEYCODES) { case (keyCode:Int, expected:KeyboardInputs) =>
      val result = KeyboardInputs.fromKeyCode(keyCode)
      result shouldBe Some(expected)
    }
  }

  it should "fail on CTRL keyCode" in {
    val result = KeyboardInputs.fromKeyCode(17)
    result shouldBe None
  }
}
