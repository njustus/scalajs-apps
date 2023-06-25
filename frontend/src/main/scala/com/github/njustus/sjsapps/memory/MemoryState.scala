package com.github.njustus.sjsapps.memory

import com.github.njustus.sjsapps.util.*
import scala.util.Random

case class MemoryState(board: List[CardDto]) {

  def updateCard(key: String)(fn: CardDto => CardDto): MemoryState = {
    val newBoard = board.map {
      case card if card.id == key => fn(card)
      case card => card
    }

    this.copy(board = newBoard)
  }

  def checkOpenCards(): MemoryState = {
    val openCards = board.filter(_.isOpen)
    if (openCards.size < 2 || openCards.size % 2 != 0) {
      this
    } else {
      val bothCards = openCards.flatMap { card =>
        openCards.find(card2 => card2.id != card.id && card2.isSameIcon(card))
          .map(card2 => List(card, card2))
          .getOrElse(Nil)
      }.toSet

      println(s"bothCards: $bothCards")

      this.copy(board = board.map {
        case card if bothCards.contains(card) => card.remove
        case card if card.isOpen => card.flip
        case card => card
      })
    }
  }
}

object MemoryState {
  val symbols: List[Icon.Props] = List(
    Icon.Props("snowflake"),
    Icon.Props("snowman"),
    Icon.Props("skiing"),
    Icon.Props("mountain"),
    Icon.Props("music"),
    Icon.Props("star"),
    Icon.Props("heart"),
    Icon.Props("hippo"),
    Icon.Props("cat"),
    Icon.Props("feather"),
    Icon.Props("dragon"),
    Icon.Props("otter"),
    Icon.Props("frog"),
    Icon.Props("horse"),
    Icon.Props("crow"),
  )

  def zero: MemoryState = {
    val cards = MemoryState.symbols
      .flatMap { icon =>
        val id = icon.icon
        List(
          CardDto(id + "-1", icon, CardState.Closed),
          CardDto(id + "-2", icon, CardState.Closed)
        )
      }

    require(cards.distinctBy(_.id).size == cards.size, "Expected unique IDS per card")

    MemoryState(Random.shuffle(Random.shuffle(cards)))
  }

}
