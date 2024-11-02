package com.github.njustus.sjsapps.tetris

import com.github.njustus.sjsapps.shared.Coordinate
import cats.syntax.semigroup.*
import com.github.njustus.sjsapps.tetris.TetrisColor.Green

case class TetrisPiece(coordinates: List[Coordinate], color: TetrisColor) {
  def withDelta(c: Coordinate): TetrisPiece = this.copy(
    coordinates = coordinates.map { origin =>
      origin |+| c
    }
  )

  def contains(c: Coordinate): Boolean = coordinates.toSet.contains(c)
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

case class TetrisBoardState(board: List[List[TetrisCell]], currentPiece: Option[TetrisPiece]) {
  lazy val renderedBoard: List[List[TetrisCell]] = board.zipWithIndex.map { (row, rowIdx) =>
    row.zipWithIndex.map {
//      case (tc@TetrisCell.Colored, _) => tc
      case (TetrisCell.Empty, colIdx) =>
        currentPiece
          .filter { piece => piece.contains(colIdx -> rowIdx) }
          .map{ it => TetrisCell.Colored(it.color) }
        .getOrElse(TetrisCell.Empty)
    }
  }
}

object TetrisBoardState {
  private val WIDTH  = 20
  private val HEIGHT = 25

  def zero: TetrisBoardState = {
    val board = List.tabulate(HEIGHT) { _ =>
      List.tabulate(WIDTH) { _ => TetrisCell.Empty
      }
    }

    TetrisBoardState(board, None)
  }

  def tick(state: TetrisBoardState): TetrisBoardState = {
    val piece = state.currentPiece.getOrElse(square(Green))
      .withDelta(0 -> 1)

    // TODO: out-of-range; collision check 
    state.copy(currentPiece = Some(piece))
  }

  private def piece(coordinates: Coordinate*): TetrisColor => TetrisPiece = TetrisPiece(coordinates.toList, _)

  val square: TetrisColor => TetrisPiece = piece(
    (0, 0),
    (1, 0),
    (0, 1),
    (1, 1)
  )
}
