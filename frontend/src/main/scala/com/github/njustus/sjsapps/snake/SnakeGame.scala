package com.github.njustus.sjsapps.snake

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom
import org.scalajs.dom.{Window, console}

object SnakeGame {

  // TODO move snake in interval/game-loop

  type Props = Unit

  private def renderFn(props: Props, state: Hooks.UseState[SnakeGameState]): VdomNode = {
    def onKeyUp(value: SyntheticKeyboardEvent[_]): IO[Unit] = IO.println(s"key pressed ${value.key}")

    <.div(
      ^.className := "snake-game",
      <.div(
        ^.className := "board columns",
        state.value.board.grid.zipWithIndex.map { (column, colIdx) =>
          <.div(
            ^.className := "column",
            ^.key       := "col-" + colIdx,
            column.zipWithIndex.map { (cell, rowIdx) =>
              <.div(
                ^.className := s"cell ${cell.cssClasses}",
                ^.key       := colIdx + "-" + rowIdx,
                ^.onKeyUp ==> ((ev: SyntheticKeyboardEvent[_]) => onKeyUp(ev)),
                cell.show
              )
            }.toVdomArray
          )
        }.toVdomArray
      ),
      <.div(s"keypress ${state.value.keyPress}")
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(SnakeGameState.zero)
    .useEffectOnMountBy { (props, state) =>
      SyncIO {
        dom.window.addEventListener(
          "keydown",
          (ev: SyntheticKeyboardEvent[_]) => {
            KeyboardInputs.fromKeyBoardEvent(ev).foreach { key =>
              println(s"key $key")
              state.modState(SnakeGameState.handleKeypress(key)).unsafeRunSync()
            }
          }
        )
      }
    }
    .render(renderFn)
}
