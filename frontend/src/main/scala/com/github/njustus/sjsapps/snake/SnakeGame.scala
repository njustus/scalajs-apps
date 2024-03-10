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

  private def renderBoard(state: SnakeGameState): VdomNode = {
    <.div(
      ^.className := "board columns",
      state.board.grid.zipWithIndex.map { (column, colIdx) =>
        <.div(
          ^.className := "column",
          ^.key       := "col-" + colIdx,
          column.zipWithIndex.map { (cell, rowIdx) =>
            <.div(
              ^.className := s"cell ${cell.cssClasses}",
              ^.key       := colIdx + "-" + rowIdx
            )
          }.toVdomArray
        )
      }.toVdomArray
    )
  }

  private def renderHighscore(state: SnakeGameState): VdomNode = {
    <.div(
      ^.className := "column",
      <.h4(
        s"GameOver! Your Highscore is: ${state.highScore}",
        s"GameState: ${state.board}"
      )
    )
  }

  private def renderFn(props: Props, state: Hooks.UseState[SnakeGameState]): VdomNode = {
    <.div(
      ^.className := "snake-game columns is-centered",
      if (state.value.isGameOver) renderHighscore(state.value)
      else renderBoard(state.value),
      <.div(
        ^.className := "column",
        <.div(s"keypress ${state.value.snakeDirection}"),
        <.div(s"Highscore ${state.value.highScore}")
      )
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(SnakeGameState.zero)
    .useEffectOnMountBy { (props, state) =>
      SyncIO {
        dom.window.setInterval(
          () => state.modState(SnakeGameState.tick).unsafeRunSync(),
          props.tickSpeed.toMillis
        )

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
