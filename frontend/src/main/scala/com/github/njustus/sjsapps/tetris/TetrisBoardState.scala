package com.github.njustus.sjsapps.tetris

import com.github.njustus.sjsapps.shared.{Coordinate, KeyboardInputs}
import cats.syntax.semigroup.*
import com.github.njustus.sjsapps.tetris.models.*

case class TetrisBoardState(board: List[List[TetrisCell]], currentPiece: TetrisPiece) {
  private val height = board.size
  private val width = board.head.size

  lazy val renderedBoard: List[List[TetrisCell]] = board.zipWithIndex.map { (row, rowIdx) =>
    row.zipWithIndex.map {
//      case (tc@TetrisCell.Colored, _) => tc
      case (TetrisCell.Empty, colIdx) =>
        if(currentPiece.contains(colIdx -> rowIdx))
          TetrisCell.Colored(currentPiece.color)
        else TetrisCell.Empty
    }
  }

  def movePiece(delta: Coordinate): TetrisBoardState = {
    val newPiece = currentPiece.withDelta(delta)

    if(isPieceWithinBounds(newPiece))
      this.copy(currentPiece = newPiece)
    else this
  }

  private def isFree(c: Coordinate): Boolean =
    (c.y >= 0 && c.y < height) &&
      (c.x >= 0 && c.x < width) &&
      (board(c.y)(c.x) == Empty)


  def isPieceWithinBounds(piece: TetrisPiece): Boolean = piece.forall { c =>
    println(s"coordinate: $c free: ${isFree(c)}")
    isFree(c)
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

    TetrisBoardState(board, TetrisPiece.pieceL(Green))
  }

  def tick(state: TetrisBoardState): TetrisBoardState = {
    val piece = state.currentPiece
      .withDelta(0 -> 1)

    // TODO: out-of-range; collision check
    if(state.isPieceWithinBounds(piece))
      state.copy(currentPiece = piece)
    else state
  }

  def handleKeypress(ev: KeyboardInputs)(state: TetrisBoardState): TetrisBoardState = ev match {
    case KeyboardInputs.Left => state.movePiece(ev.delta)
    case KeyboardInputs.Right => state.movePiece(ev.delta)
    case _ => state //unused
  }
}
