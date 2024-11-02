package com.github.njustus.sjsapps.tetris

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom
import scala.concurrent.duration.Duration
import scala.concurrent.duration.*
import scala.language.postfixOps

object TetrisGame {

  case class Props(tickSpeed: Duration = 0.5 second)
  type State = TetrisBoardState

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    <.div(^.className := "tetris-game flex",
      <.div(^.className := "w-3/4 board grow border border-red-400",
        TetrisBoard.render(state.value.board)
      ),
      <.div(^.className := "score-details grow border border-blue-400")
    )
  }

  def render(): VdomNode = component(TetrisGame.Props())
  
  private val component = ScalaFnComponent
    .withHooks[Props]
    .useState(TetrisBoardState.zero)
    .render(renderFn)
}
