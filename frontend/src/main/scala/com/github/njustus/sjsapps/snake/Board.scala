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
                 fruit: Board.Fruit,
                 snake: Board.Snake) {
  def isSnakeAtFruit: Boolean = snake.head == fruit
  
  def grid: List[List[Cell]] =
    List.tabulate(size) { columnIdx =>
      List.tabulate(size) {
        case rowIdx if fruit.isAt(columnIdx, rowIdx) => Cell.Fruit
        case rowIdx if snake.contains(Coordinate(columnIdx, rowIdx)) => Cell.Snake
        case _ => Cell.Empty
      }
    }
}

object Board {
  type Snake = List[Coordinate]
  type Fruit = Coordinate

  def zero: Board = {
    val size     = 40
    val snakeIdx = size / 2
    val fruitIdx = 16

    val  snake = List(
      Coordinate(snakeIdx, snakeIdx),
      Coordinate(snakeIdx-1, snakeIdx)
    )

    val fruit = Coordinate(fruitIdx, snakeIdx)
    Board(size, fruit, snake)
  }
  
  def newFruit(boardSize: Int): Fruit =
    Coordinate(
      scala.util.Random.between(0, boardSize),
      scala.util.Random.between(0, boardSize)
    )
}
