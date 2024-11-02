package com.github.njustus.sjsapps.tetris

import com.github.njustus.sjsapps.shared.Coordinate

case class TetrisPiece(coordinates: List[Coordinate], color: TetrisColor) {

}

enum TetrisColor {
  case Red    extends TetrisColor
  case Blue   extends TetrisColor
  case Yellow extends TetrisColor
  case Green  extends TetrisColor
}

enum TetrisCell {
  case Empty                       extends TetrisCell
  case Colored(color: TetrisColor) extends TetrisCell

  def cssClass: String = this match {
    case Empty          => ""
    case Colored(color) => s"cell--${color.toString.toLowerCase}"
  }
}

case class TetrisBoardState(board: List[List[TetrisCell]])

object TetrisBoardState {
  private val WIDTH  = 20
  private val HEIGHT = 25

  def zero: TetrisBoardState = {
    val board = List.tabulate(HEIGHT) { _ =>
      List.tabulate(WIDTH) {
        case 1 => TetrisCell.Colored(TetrisColor.Red)
        case 2 => TetrisCell.Colored(TetrisColor.Blue)
        case 3 => TetrisCell.Colored(TetrisColor.Green)
        case 4 => TetrisCell.Colored(TetrisColor.Yellow)
        case _ => TetrisCell.Empty
      }
    }

    TetrisBoardState(board)
  }

  private def piece(coordinates: Coordinate*): TetrisColor => TetrisPiece = TetrisPiece(coordinates.toList, _)

  val square: TetrisColor => TetrisPiece = piece(
    (0, 0),
    (1, 0),
    (0, 1),
    (1, 1)
  )
}
