package com.github.njustus.sjsapps.complete4

enum PlayerColor {
  case Blue
  case Red
  case None
}

case class Player(name: String, color: PlayerColor)

case class Chip(color: PlayerColor) {
  def cssClass: String = color match {
    case PlayerColor.Blue => "complete-4-chip-a"
    case PlayerColor.Red =>  "complete-4-chip-b"
    case PlayerColor.None => "complete-4-chip-empty"
  }
}
object Chip {
  def none: Chip = Chip(PlayerColor.None)
}

case class Complete4State(chipColumns: List[List[Chip]],
                         players: List[Player])

object Complete4State {
  def zero: Complete4State =
    val columns = List.fill(4) {
      List.fill(6) {
        Chip.none
      }
    }

    val players = List(
      Player("player-A", PlayerColor.Blue),
      Player("player-B", PlayerColor.Red)
    )

    Complete4State(columns, players)
}
