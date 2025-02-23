package com.github.njustus.sjsapps.tetris

import com.github.njustus.sjsapps.shared.{Coordinate, KeyboardInputs}
import cats.syntax.semigroup.*
import com.github.njustus.sjsapps.tetris.TetrisColor.{Green, Yellow}

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
  
  def movePiece(delta: Coordinate): TetrisBoardState = {
    val newPiece = currentPiece.map { piece =>
      piece.withDelta(delta)
    }

    this.copy(currentPiece = newPiece)
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
    val piece = state.currentPiece.getOrElse(pieceL(Green))
      .withDelta(0 -> 1)

    // TODO: out-of-range; collision check
    state.copy(currentPiece = Some(piece))
  }

  def handleKeypress(ev: KeyboardInputs)(state: TetrisBoardState): TetrisBoardState = ev match {
    case KeyboardInputs.Left => state.movePiece(ev.delta)
    case KeyboardInputs.Right => state.movePiece(ev.delta)
    case _ => state //unused
  }
  
  private def piece(coordinates: Coordinate*): TetrisColor => TetrisPiece = TetrisPiece(coordinates.toList, _)

  val pieceO: TetrisColor => TetrisPiece = piece(
    (0, 0),
    (1, 0),
    (0, 1),
    (1, 1)
  )

  val pieceI: TetrisColor => TetrisPiece = piece(
    (0,0),
    (0,1),
    (0,2),
    (0,3),
  )

  val pieceL: TetrisColor => TetrisPiece = piece(
    (0,0),
    (0,1),
    (0,2),
    (1,2)
  )

  val pieceJ = piece (
    (1,0),
    (1,1),
    (1,2),
    (0,2)
  )

  val pieceT: TetrisColor => TetrisPiece = piece(
    (0,0),
    (1,0),
    (2,0),
    (1,1),
  )
}
