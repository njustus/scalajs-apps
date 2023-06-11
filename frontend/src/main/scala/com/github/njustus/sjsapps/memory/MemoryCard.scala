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
  enum CardState:
    case Open, Closed, Removed

  case class Props(id:String, iconProps: Icon.Props, cardState: CardState)

  private def openCard(iconProps:Icon.Props) =
    <.div(^.className:="box memory-card",
      Icon.component(iconProps)
    )

  private def closedCard(iconProps:Icon.Props) =
    <.div(^.className:="box memory-card memory-card--closed",
    )

  private def removedCard(iconProps:Icon.Props) =
    <.div(^.className:="memory-card memory-card--removed",
    )

  def renderFn(props: Props): VdomNode = props.cardState match {
    case CardState.Open => openCard(props.iconProps)
    case CardState.Closed => closedCard(props.iconProps)
    case CardState.Removed => removedCard(props.iconProps)
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
