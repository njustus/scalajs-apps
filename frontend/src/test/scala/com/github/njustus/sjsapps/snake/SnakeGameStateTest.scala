package com.github.njustus.sjsapps.snake

import com.github.njustus.sjsapps.BaseTest
import Coordinate.given
import cats.Monoid
import cats.syntax.monoid.*

class SnakeGameStateTest extends BaseTest {
  val state = SnakeGameState.zero

  val SNAKE_START = Coordinate(20, 20)

  "The SnakeState" should "contain a snake" in {
    val result = state.board.columns.map { row =>
      row.find(_ == Cell.Snake)
    }.flatten

    result should have size(1)
    result.head shouldBe Cell.Snake
  }

  it should "know the snake's coordinate" in {
    state.board.snakePosition shouldBe Some(SNAKE_START)
  }

  it should "move the snake" in {
    val directions = Seq(
      KeyboardInputs.Up -> Coordinate(20, 19),
      KeyboardInputs.Down -> Coordinate(20, 21),
      KeyboardInputs.Left -> Coordinate(19, 20),
      KeyboardInputs.Right -> Coordinate(21, 20),
    )

    forAll(directions) { (input, expected) =>
      val newState = SnakeGameState.handleKeypress(input)(state)
      newState.board.snakePosition shouldBe Some(expected)
    }
  }

  "The coordinate monoid" should "have an identity" in {
    Monoid[Coordinate].empty shouldBe Coordinate(0,0)
  }

  it should "combine 2 coordinates" in {
    val examples = List(
      (Coordinate(0,0), Coordinate(0,0), Coordinate(0,0)),
      (Coordinate(1,1), Coordinate(1,1), Coordinate(2,2)),
      (Coordinate(1,0), Coordinate(1,0), Coordinate(2,0)),
      (Coordinate(0,1), Coordinate(0,1), Coordinate(0,2)),
      (Coordinate(50,50), Coordinate(10,0), Coordinate(60,50))
    )

    forAll(examples) { case (c1, c2, expected) =>
      val result = c1 |+| c2
      result shouldBe expected
    }
  }
}
