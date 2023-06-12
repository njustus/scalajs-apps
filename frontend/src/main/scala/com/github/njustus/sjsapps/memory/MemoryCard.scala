package com.github.njustus.sjsapps.memory

import com.github.njustus.sjsapps.util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object MemoryCard {

  case class Props(cardDto:CardDto,
    onClick: String => SyncIO[Unit])

  private def openCard(props:Props) =
    <.div(^.className:="box memory-card memory-card--open",
      Icon.component(props.cardDto.iconProps),
      ^.onClick --> props.onClick(props.cardDto.id)
    )

  private def closedCard(props:Props) =
    <.div(^.className:="box memory-card memory-card--closed",
      ^.onClick --> props.onClick(props.cardDto.id)
    )

  private def removedCard(props:Props) =
    <.div(^.className:="memory-card memory-card--removed",
      ^.onClick --> props.onClick(props.cardDto.id)
    )

  def renderFn(props: Props): VdomNode = props.cardDto.cardState match {
    case CardState.Open => openCard(props)
    case CardState.Closed => closedCard(props)
    case CardState.Removed => removedCard(props)
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
