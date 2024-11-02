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
import com.github.njustus.sjsapps.shared.*

object TetrisGame {

  case class Props(tickSpeed: Duration = 0.5 second)
  type State = TetrisBoardState

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    <.div(^.className := "tetris-game flex",
      <.div(^.className := "w-3/4 board grow",
        TetrisBoard.render(state.value.renderedBoard)
      ),
      <.div(^.className := "score-details grow")
    )
  }

  def render(): VdomNode = component(TetrisGame.Props())

  private val component = ScalaFnComponent
    .withHooks[Props]
    .useState(TetrisBoardState.zero)
        .useEffectOnMountBy { (props, state) =>
      SyncIO {
        dom.window.setInterval(
          () => state.modState(TetrisBoardState.tick).unsafeRunSync(),
          props.tickSpeed.toMillis
        )

        dom.window.addEventListener(
          "keydown",
          (ev: SyntheticKeyboardEvent[?]) => {
            KeyboardInputs.fromKeyBoardEvent(ev).foreach { key =>
              println(s"key $key")
//              state.modState(SnakeGameState.handleKeypress(key)).unsafeRunSync()
            }
          }
        )
      }
    }
    .render(renderFn)
}
