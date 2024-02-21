package com.github.njustus.sjsapps.snake

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom
import org.scalajs.dom.{Window, console}

import scala.concurrent.duration.Duration
import scala.concurrent.duration.*
import scala.language.postfixOps

object SnakeGame {

  case class Props(tickSpeed: Duration = 0.5 second)

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
      <.div(s"keypress ${state.value.snakeDirection}")
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(SnakeGameState.zero)
    .useEffectOnMountBy { (props, state) =>
      SyncIO {
        dom.window.setInterval(() => {
          println(s"moving snake..")
          state.modState(SnakeGameState.tick).unsafeRunSync()
        }, props.tickSpeed.toMillis)

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
