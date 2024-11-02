package com.github.njustus.sjsapps.tetris

enum TetrisColor {
  case Red extends TetrisColor
  case Blue extends TetrisColor
  case Yellow extends TetrisColor
  case Green extends TetrisColor
}

enum TetrisCell {
  case Empty extends TetrisCell
  case Colored(color: TetrisColor) extends TetrisCell
}

case class TetrisBoardState(board: List[List[TetrisCell]])

object TetrisBoardState {
  private val WIDTH = 20
  private val HEIGHT = 25

  def zero: TetrisBoardState = {
    val board = List.tabulate(HEIGHT) { _ =>
      List.tabulate(WIDTH) { _ =>
        TetrisCell.Empty
      }
    }

    TetrisBoardState(board)
  }
}
