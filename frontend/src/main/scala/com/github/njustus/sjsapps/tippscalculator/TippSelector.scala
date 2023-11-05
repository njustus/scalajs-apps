package com.github.njustus.sjsapps.tippscalculator

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import japgolly.scalajs.react.hooks.Hooks.UseState

object TippSelector {

  case class Props(selectedTipPercentage: Option[Float], tipSelected: Float => IO[Unit])

  private def percentButton(props: Props)(percent: Int) = {
    val tip         = (percent * 0.01f)
    val activeColor = if (props.selectedTipPercentage.contains(tip)) "active" else ""

    <.button(
      s"$percent %",
      ^.className := s"button is-info is-outlined ${activeColor}",
      ^.onClick --> props.tipSelected(tip)
    )
  }

  def renderFn(props: Props): VdomNode = {
    val btn = percentButton(props)(_)

    <.div(
      ^.className := "buttons has-addons is-justify-content-center",
      btn(10),
      btn(20),
      btn(30),
      btn(50)
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
