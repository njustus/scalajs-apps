package com.github.njustus.sjsapps.memory

import com.github.njustus.sjsapps.util.Icon

case class CardDto(
    id: String,
    iconProps: Icon.Props,
    cardState: CardState
) {
  def flip: CardDto = cardState match {
    case CardState.Open   => this.copy(cardState = CardState.Closed)
    case CardState.Closed => this.copy(cardState = CardState.Open)
    case _                => this
  }

  def remove: CardDto = this.copy(cardState = CardState.Removed)

  def isOpen: Boolean    = cardState == CardState.Open
  def isClosed: Boolean  = cardState == CardState.Closed
  def isRemoved: Boolean = cardState == CardState.Removed

  def icon: String = iconProps.icon

  def isSameIcon(other: CardDto): Boolean = this.icon == other.icon

  override def equals(obj: Any): Boolean = obj match {
    case card: CardDto => this.id == card.id
    case _             => false
  }

  override def hashCode(): Int = this.id.hashCode
}

enum CardState:
  case Open, Closed, Removed
