package com.github.njustus.sjsapps.complete4

import cats.data.OptionT
import com.github.njustus.sjsapps.util.SeqUtils
import cats.instances.all.*
import cats.Id
import com.github.njustus.sjsapps.complete4.Complete4State.CHIPS_NEEDDED_TO_WIN
import wvlet.log.LogSupport

enum PlayerColor {
  case Blue
  case Red
  case None
}

case class Player(name: String, color: PlayerColor) {
  def descriptionCssClass: String = color match {
    case PlayerColor.Blue => "player-a-color"
    case PlayerColor.Red  => "player-b-color"
    case _                => ""
  }
}

case class Chip(id: String, color: PlayerColor) {
  def updateColor(color: PlayerColor): Chip = this.copy(color = color)

  def isEmpty: Boolean = color == PlayerColor.None

  def cssClass: String = color match {
    case PlayerColor.Blue => "complete-4-chip-a"
    case PlayerColor.Red  => "complete-4-chip-b"
    case PlayerColor.None => "complete-4-chip-empty"
  }
}

object Chip {
  def none(id: String): Chip = Chip(id, PlayerColor.None)
}

case class Complete4State(
    chipColumns: List[List[Chip]],
    players: List[Player],
    currentPlayersId: String,
    currentWinner: Option[Player]
) extends LogSupport {
  require(players.exists(_.name == currentPlayersId), "expected 'currentPlayersId' to be one of player's name")

  private def isWinner(columnIdx: Int): Option[Player] = {
    val nonEmptyChipRows = chipColumns(columnIdx).zipWithIndex.filter { (chip, rowIdx) => !chip.isEmpty }.map(_._2)
    nonEmptyChipRows.flatMap { rowIdx => isWinningMove(columnIdx, rowIdx).toList }.headOption
  }

  def updateWinner(columnIdx: Int): Complete4State = this.copy(currentWinner = isWinner(columnIdx))

  def addChip(columnIdx: Int): Complete4State = addChip(columnIdx, currentPlayersColor)

  def addChip(columnIdx: Int, color: PlayerColor): Complete4State = {
    val updatedColumns = chipColumns.zipWithIndex.map {
      case (column, idx) if idx == columnIdx =>
        val firstEmptyChipIndex = column.indexWhere(chip => chip.isEmpty)
        info(s"updating row: $idx and chipIdx $firstEmptyChipIndex to $color")

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

  def getPlayerByColor(color: PlayerColor): Option[Player] = this.players.find(p => p.color == color)

  private def currentPlayersColor: PlayerColor = players.find(_.name == currentPlayersId).get.color

  private def nextPlayer: Player = players match {
    case head :: second :: Nil if head.name == currentPlayersId => second
    case first :: last :: Nil if last.name == currentPlayersId  => first
    case _                                                      => players.head
  }

  private def isWinningMove(columnIdx: Int, rowIdx: Int): Option[Player] = {
    val chipsUp = (rowIdx until rowIdx + CHIPS_NEEDDED_TO_WIN by 1)
      .map(getChip(columnIdx))
      .flatMap(_.toList)

    val chipsDown = (rowIdx until rowIdx - CHIPS_NEEDDED_TO_WIN by -1)
      .map(getChip(columnIdx))
      .flatMap(_.toList)

    val chipsRight = (columnIdx until columnIdx + CHIPS_NEEDDED_TO_WIN by 1)
      .map(colIdx => getChip(colIdx)(rowIdx))
      .flatMap(_.toList)

    val chipsLeft = (columnIdx until columnIdx - CHIPS_NEEDDED_TO_WIN by -1)
      .map(colIdx => getChip(colIdx)(rowIdx))
      .flatMap(_.toList)

    println(s"up: $chipsUp")
    println(s"down: $chipsDown")
    println(s"left: $chipsLeft")
    println(s"right: $chipsRight")

    if (chipsUp.size >= 4 && SeqUtils.allEqual(chipsUp.map(_.color)))
      getPlayerByColor(chipsUp.head.color)
    else if (chipsDown.size >= 4 && SeqUtils.allEqual(chipsDown.map(_.color)))
      getPlayerByColor(chipsDown.head.color)
    else if (chipsRight.size >= 4 && SeqUtils.allEqual(chipsRight.map(_.color)))
      getPlayerByColor(chipsRight.head.color)
    else if (chipsLeft.size >= 4 && SeqUtils.allEqual(chipsLeft.map(_.color)))
      getPlayerByColor(chipsLeft.head.color)
    else None
  }

  private def getChip(colIdx: Int)(rowIdx: Int): Option[Chip] =
    for {
      column <- SeqUtils.getIdx(colIdx)(chipColumns)
      cell   <- SeqUtils.getIdx(rowIdx)(column)
    } yield cell

}

object Complete4State {
  val CHIPS_NEEDDED_TO_WIN = 4

  def zero: Complete4State =
    val columns = List.tabulate(7) { colIdx =>
      List.tabulate(6) { rowIdx =>
        Chip.none(s"$colIdx-$rowIdx")
      }
    }

    val players = List(
      Player("player-A", PlayerColor.Blue),
      Player("player-B", PlayerColor.Red)
    )

    Complete4State(columns, players, players.head.name, None)
}
