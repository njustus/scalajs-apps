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
  case class Props(iconProps:Icon.Props, isHidden:Boolean)

  def renderFn(props: Props): VdomNode = {
    <.div(^.className:="box memory-card",
      Icon.component(props.iconProps)
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}