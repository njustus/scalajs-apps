package com.github.njustus.sjsapps.complete4

enum PlayerColor {
  case Blue
  case Red
  case None
}

case class Player(name: String, color: PlayerColor) {
  def descriptionCssClass: String = color match {
    case PlayerColor.Blue => "player-a-color"
    case PlayerColor.Red => "player-b-color"
    case _ => ""
  }
}

case class Chip(id: String, color: PlayerColor) {
  def updateColor(color: PlayerColor): Chip = this.copy(color = color)

  def isEmpty: Boolean = color == PlayerColor.None

  def cssClass: String = color match {
    case PlayerColor.Blue => "complete-4-chip-a"
    case PlayerColor.Red => "complete-4-chip-b"
    case PlayerColor.None => "complete-4-chip-empty"
  }
}

object Chip {
  def none(id: String): Chip = Chip(id, PlayerColor.None)
}

case class Complete4State(chipColumns: List[List[Chip]],
                          players: List[Player],
                          currentPlayersId: String) {
  require(players.exists(_.name==currentPlayersId), "expected 'currentPlayersId' to be one of player's name")

  def addChip(columnIdx: Int): Complete4State = addChip(columnIdx, currentPlayersColor)

  def addChip(columnIdx: Int, color: PlayerColor): Complete4State = {
    val updatedColumns = chipColumns.zipWithIndex.map {
      case (column, idx) if idx == columnIdx =>
        val firstEmptyChipIndex = column.indexWhere(chip => chip.isEmpty)
        println(s"updating row: $idx and chipIdx $firstEmptyChipIndex to $color")

        if (firstEmptyChipIndex < 0) column
        else {
          val flippedChip = column(firstEmptyChipIndex).updateColor(color)
          column.updated(firstEmptyChipIndex, flippedChip)
        }
      case (column, _) => column
    }

    this.copy(
      chipColumns = updatedColumns,
      currentPlayersId = nextPlayer.name
    )
  }



  def isCurrentPlayer(player: Player): Boolean =
    currentPlayersId == player.name

  private def currentPlayersColor: PlayerColor = players.find(_.name==currentPlayersId).get.color

  private def nextPlayer: Player = players match {
    case head :: second :: Nil if head.name == currentPlayersId => second
    case first :: last :: Nil if last.name == currentPlayersId => first
    case _ => players.head
  }
}

object Complete4State {
  def zero: Complete4State =
    val columns = List.tabulate(6) { colIdx =>
      List.tabulate(4) { rowIdx =>
        Chip.none(s"$colIdx-$rowIdx")
      }
    }

    val players = List(
      Player("player-A", PlayerColor.Blue),
      Player("player-B", PlayerColor.Red)
    )

    Complete4State(columns, players, players.head.name)
}
