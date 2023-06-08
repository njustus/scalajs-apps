package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object MemoryCard {
  case class Props(symbol:String, isHidden:Boolean)

  def renderFn(props: Props): VdomNode = {
    <.div(^.className:="memory-card",
      props.symbol
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
