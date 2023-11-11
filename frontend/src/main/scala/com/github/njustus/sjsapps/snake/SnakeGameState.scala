package com.github.njustus.sjsapps.snake

case class SnakeGameState(
                         board: Board,
                         keyPress: Option[KeyboardInputs]
                         )

object SnakeGameState {

  def zero: SnakeGameState = SnakeGameState(Board.zero, None)

  def handleKeypress(ev:KeyboardInputs)(gs: SnakeGameState): SnakeGameState =
    gs.copy(keyPress = Some(ev))

}
