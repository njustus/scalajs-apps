package com.github.njustus.sjsapps.complete4


import com.github.njustus.sjsapps.util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object ChipComponent {
  case class Props(chip: Chip)

  private def renderFn(props: Props): VdomNode =
    <.div(^.className:=s"complete-4-chip ${props.chip.cssClass}")

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }

}
