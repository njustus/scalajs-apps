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
                         ) {
  val boardSize = board.size
}

object SnakeGameState {
  private val boardLens: Lens[SnakeGameState, Board] = GenLens[SnakeGameState](_.board)
  private val snakeLens: Lens[SnakeGameState, Board.Snake] = GenLens[SnakeGameState](_.board.snake)
  private val fruitLens: Lens[SnakeGameState, Board.Fruit] = GenLens[SnakeGameState](_.board.fruit)
  private val directionLens = GenLens[SnakeGameState](_.snakeDirection)

  //TODO detect out-of-grid and begin at start
  def zero: SnakeGameState = SnakeGameState(Board.zero, KeyboardInputs.Right)

  def handleKeypress(ev: KeyboardInputs): SnakeGameState => SnakeGameState = directionLens.set(ev)

  def tick(gs: SnakeGameState): SnakeGameState =
    if(gs.board.isSnakeAtFruit) {
      eatFruit(gs)
    } else {
      val delta = directionDelta(gs.snakeDirection)
      moveSnake(delta)(gs)
    }

  private def eatFruit(gs: SnakeGameState): SnakeGameState = {
    val newFruit = Board.newFruit(gs.board.size)
    boardLens.modify { board =>
      board.copy(
        fruit = newFruit,
        snake = board.fruit :: board.snake
      )
    }(gs)
  }

  private def moveSnake(delta: Coordinate)(gs: SnakeGameState): SnakeGameState =
    snakeLens.modify { snake =>
      val tail = snake.init
      val newHead = (snake.head |+| delta) match {
        case Coordinate(x, y) if(x < 0) => Coordinate(gs.boardSize-1, y)
        case Coordinate(x, y) if(x >= gs.boardSize) => Coordinate(0, y)
        case Coordinate(x, y) if(y < 0) => Coordinate(x, gs.boardSize-1)
        case Coordinate(x, y) if(y >= gs.boardSize) => Coordinate(x, 0)
        case c => c
      }
      
      newHead :: tail
    }(gs)

  private def directionDelta(input: KeyboardInputs): Coordinate = input match {
    case KeyboardInputs.Up    => Coordinate(0, -1)
    case KeyboardInputs.Down  => Coordinate(0, 1)
    case KeyboardInputs.Left  => Coordinate(-1, 0)
    case KeyboardInputs.Right => Coordinate(1, 0)
  }
}
