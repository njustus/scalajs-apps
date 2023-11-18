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

case class Board(columns: List[List[Cell]]) {
  val snakePosition: Option[Coordinate] = columns.zipWithIndex.flatMap { (row, colIdx) =>
    row.zipWithIndex.collect { case (Cell.Snake, rowIdx) =>
      Coordinate(colIdx, rowIdx)
    }
  }.headOption

  def replace(position: Coordinate, cell: Cell): Board =
    val newBoard = this.columns.zipWithIndex.map { (row, colIdx) =>
      row.zipWithIndex.map {
        case (_, rowIdx) if position.isAt(colIdx, rowIdx) => cell
        case (current, _)                                 => current
      }
    }

    Board(newBoard)
}

object Board {
  def zero = {
    val size     = 40
    val snakeIdx = size / 2
    val fruitIdx = 16

    Board(List.tabulate(size) { columnIdx =>
      List.tabulate(size) {
        case rowIdx if columnIdx == snakeIdx && rowIdx == fruitIdx => Cell.Fruit
        case rowIdx if columnIdx == snakeIdx && rowIdx == snakeIdx => Cell.Snake
        case _                                                     => Cell.Empty
      }
    })
  }
}
