package com.github.njustus.sjsapps.util

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object Icon {
  case class Props(icon:String, pack:String="solid")

  def renderFn(props: Props): VdomNode = {
    <.i(^.className:=s"fa fa-${props.pack} fa-${props.icon}")
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
