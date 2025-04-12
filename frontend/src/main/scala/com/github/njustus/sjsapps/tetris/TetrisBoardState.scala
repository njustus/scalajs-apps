package com.github.njustus.sjsapps.tetris

import com.github.njustus.sjsapps.shared.{Coordinate, KeyboardInputs}
import cats.syntax.semigroup.*
import com.github.njustus.sjsapps.tetris.TetrisCell.Empty
import com.github.njustus.sjsapps.tetris.TetrisColor.{Green, Yellow}

case class TetrisPiece(coordinates: List[Coordinate], color: TetrisColor) {
  val forall: (Coordinate => Boolean) => Boolean = coordinates.forall

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
  private val height = board.size
  private val width = board.head.size

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

  def isFree(c: Coordinate): Boolean =
    (c.y >= 0 && c.y < height) &&
      (c.x >= 0 && c.x < width) &&
      (board(c.y)(c.x) == Empty)
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

    val isPieceWithinBounds = piece.forall { c =>
      println(s"coordinate: $c free: ${state.isFree(c)}")
      state.isFree(c)
    }

    // TODO: out-of-range; collision check
    if(isPieceWithinBounds)
      state.copy(currentPiece = Some(piece))
    else state
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
