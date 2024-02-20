package com.github.njustus.sjsapps.snake

import Coordinate.given
import cats.syntax.monoid.*
import monocle.Lens

import scala.concurrent.duration.Duration
import scala.concurrent.duration.*
import scala.language.postfixOps

case class SnakeGameState(
                           board: Board,
                           snakeDirection: KeyboardInputs
                         )

object SnakeGameState {
  //TODO use lens for updating state
  //TODO detect fruit collision
  def zero: SnakeGameState = SnakeGameState(Board.zero, KeyboardInputs.Right)

  def handleKeypress(ev: KeyboardInputs)(gs: SnakeGameState): SnakeGameState =
    //TODO chain calls to moveSnake
    val delta = directionDelta(ev)
    moveSnake(delta)(gs).copy(
     snakeDirection = ev
    )

  def moveSnake(gs: SnakeGameState): SnakeGameState =
    moveSnake(directionDelta(gs.snakeDirection))(gs)

  private def moveSnake(delta: Coordinate)(gs: SnakeGameState): SnakeGameState =
    val newSnake = (gs.board.snake.head |+| delta) :: gs.board.snake.init
    val newBoard = gs.board.copy(
      snake = newSnake
    )
    
    gs.copy(board = newBoard)

  private def directionDelta(input: KeyboardInputs): Coordinate = input match {
    case KeyboardInputs.Up    => Coordinate(0, -1)
    case KeyboardInputs.Down  => Coordinate(0, 1)
    case KeyboardInputs.Left  => Coordinate(-1, 0)
    case KeyboardInputs.Right => Coordinate(1, 0)
  }
}
