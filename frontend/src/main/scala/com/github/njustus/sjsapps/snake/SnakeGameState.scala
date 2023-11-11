package com.github.njustus.sjsapps.snake

import Coordinate.given
import cats.syntax.monoid.*

case class SnakeGameState(
                         board: Board,
                         keyPress: Option[KeyboardInputs]
                         )

object SnakeGameState {

  def zero: SnakeGameState = SnakeGameState(Board.zero, None)

  def handleKeypress(ev:KeyboardInputs)(gs: SnakeGameState): SnakeGameState =
    val delta = directionDelta(ev)
    gs.copy(
      board = moveSnake(delta, gs.board),
      keyPress = Some(ev),
    )

  private def moveSnake(delta: Coordinate, board: Board): Board = board.snakePosition.map { sp =>
    val newSnakePosition = sp |+| delta

    board.replace(sp, Cell.Empty)
      .replace(newSnakePosition, Cell.Snake)
  }.getOrElse(board)

  private def directionDelta(input: KeyboardInputs): Coordinate = input match {
    case KeyboardInputs.Up => Coordinate(0, -1)
    case KeyboardInputs.Down => Coordinate(0, 1)
    case KeyboardInputs.Left => Coordinate(-1, 0)
    case KeyboardInputs.Right => Coordinate(1, 0)
  }
}
