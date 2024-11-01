package com.github.njustus.sjsapps.tetris

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object TetrisWrapper {

  type Props = Unit
  type State = Unit

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    TetrisGame.component(TetrisGame.Props())
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(())
    .render(renderFn)
}
