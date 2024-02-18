package com.github.njustus.sjsapps.snake

enum Cell(val cssClasses: String) {
  case Empty extends Cell("")
  case Snake extends Cell("cell-snake")
  case Fruit extends Cell("cell-fruit")

  def show: String = this match {
    case Empty => ""
    case x     => x.toString.take(1)
  }
}

case class Board(size: Int,
                 fruit: Coordinate,
                 snake: Set[Coordinate]) {
  def grid: List[List[Cell]] =
    List.tabulate(size) { columnIdx =>
      List.tabulate(size) {
            //TODO impl snake
        case rowIdx if fruit.isAt(columnIdx, rowIdx) => Cell.Fruit
        case rowIdx if snake.contains(Coordinate(columnIdx, rowIdx)) => Cell.Snake
        case _ => Cell.Empty
      }
    }
}

object Board {
  def zero: Board = {
    val size     = 40
    val snakeIdx = size / 2
    val fruitIdx = 16

    val  snake = Coordinate(snakeIdx, snakeIdx)
    val fruit = Coordinate(fruitIdx, snakeIdx)
    Board(size, fruit, Set(snake))
  }
}
