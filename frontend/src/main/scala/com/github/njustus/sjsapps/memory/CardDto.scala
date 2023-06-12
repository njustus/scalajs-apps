package com.github.njustus.sjsapps.memory

import com.github.njustus.sjsapps.util.Icon

case class CardDto(
  id:String,
  iconProps:Icon.Props,
  cardState:CardState
) {
  def flip: CardDto = cardState match {
      case CardState.Open => this.copy(cardState= CardState.Closed)
      case CardState.Closed => this.copy(cardState= CardState.Open)
      case _ => this
  }
}

enum CardState:
  case  Open, Closed, Removed
