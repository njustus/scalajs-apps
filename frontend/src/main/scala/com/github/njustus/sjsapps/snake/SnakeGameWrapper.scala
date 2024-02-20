package com.github.njustus.sjsapps.snake

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object SnakeGameWrapper {

  type Props = Unit
  type State = Unit

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    SnakeGame.component(SnakeGame.Props())
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(())
    .render((p, s) => renderFn(p, s))
}
