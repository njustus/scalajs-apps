package com.github.njustus.sjsapps.snake

enum Cell(val cssClasses: String) {
  case Empty extends Cell("")
  case Snake extends Cell("cell-snake")
  case Fruit extends Cell("cell-fruit")
}

case class Board(columns: List[List[Cell]])

object Board {
  def zero = {
    val size = 20
    val snakeIdx = size/2
    val fruitIdx = 16

    Board(List.tabulate(size) { columnIdx =>
      List.tabulate(size) {
            case rowIdx if columnIdx == snakeIdx && rowIdx == fruitIdx => Cell.Fruit
        case rowIdx if columnIdx == snakeIdx && rowIdx == snakeIdx => Cell.Snake
        case _ => Cell.Empty
      }
    })
  }
}
