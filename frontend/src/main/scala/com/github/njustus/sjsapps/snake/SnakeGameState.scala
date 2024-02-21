package com.github.njustus.sjsapps.snake

import Coordinate.given
import cats.syntax.monoid.*
import monocle.Lens

import scala.concurrent.duration.Duration
import scala.concurrent.duration.*
import scala.language.postfixOps
import monocle.macros.GenLens

case class SnakeGameState(
                           board: Board,
                           snakeDirection: KeyboardInputs
                         )

object SnakeGameState {
  private val snakeLens = GenLens[SnakeGameState](_.board.snake)
  private val fruitLens = GenLens[SnakeGameState](_.board.fruit)

  //TODO detect fruit collision
  //TODO detect out-of-grid and begin at start
  def zero: SnakeGameState = SnakeGameState(Board.zero, KeyboardInputs.Right)

  def handleKeypress(ev: KeyboardInputs)(gs: SnakeGameState): SnakeGameState =
    val delta = directionDelta(ev)
    moveSnake(delta)(gs).copy(snakeDirection = ev)

  def moveSnake(gs: SnakeGameState): SnakeGameState = moveSnake(directionDelta(gs.snakeDirection))(gs)

  private def moveSnake(delta: Coordinate): SnakeGameState => SnakeGameState =
    snakeLens.modify { snake =>
      val tail = snake.init
      (snake.head |+| delta) :: tail
    }

  private def directionDelta(input: KeyboardInputs): Coordinate = input match {
    case KeyboardInputs.Up    => Coordinate(0, -1)
    case KeyboardInputs.Down  => Coordinate(0, 1)
    case KeyboardInputs.Left  => Coordinate(-1, 0)
    case KeyboardInputs.Right => Coordinate(1, 0)
  }
}
