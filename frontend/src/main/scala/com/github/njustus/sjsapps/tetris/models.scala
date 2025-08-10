package com.github.njustus.sjsapps.tetris

import cats.syntax.semigroup.*
import com.github.njustus.sjsapps.shared.Coordinate

object models {
  export TetrisCell.*
  export TetrisColor.*

  case class TetrisPiece(coordinates: List[Coordinate], color: TetrisColor) {
    val forall: (Coordinate => Boolean) => Boolean = coordinates.forall

    def withDelta(c: Coordinate): TetrisPiece = this.copy(
      coordinates = coordinates.map { origin =>
        origin |+| c
      }
    )

    def contains(c: Coordinate): Boolean = coordinates.toSet.contains(c)
  }

  object TetrisPiece {
    private def piece(coordinates: Coordinate*): TetrisColor => TetrisPiece = TetrisPiece(coordinates.toList, _)

    val pieceO: TetrisColor => TetrisPiece = piece(
      (0, 0),
      (1, 0),
      (0, 1),
      (1, 1)
    )

    val pieceI: TetrisColor => TetrisPiece = piece(
      (0, 0),
      (0, 1),
      (0, 2),
      (0, 3)
    )

    val pieceL: TetrisColor => TetrisPiece = piece(
      (0, 0),
      (0, 1),
      (0, 2),
      (1, 2)
    )

    val pieceJ = piece(
      (1, 0),
      (1, 1),
      (1, 2),
      (0, 2)
    )

    val pieceT: TetrisColor => TetrisPiece = piece(
      (0, 0),
      (1, 0),
      (2, 0),
      (1, 1)
    )
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

}
