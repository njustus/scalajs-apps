package com.github.njustus.sjsapps.snake

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom
import org.scalajs.dom.{Window, console}

object SnakeGame {

  type Props = Unit

  private def renderFn(props: Props, state: Hooks.UseState[Board]): VdomNode = {
    def onKeyUp(value: SyntheticKeyboardEvent[_]): IO[Unit] = IO.println(s"key pressed ${value.key}")

    <.div(^.className:="snake-game",
      <.div(^.className:="board columns",
        state.value.columns.zipWithIndex.map { (column, colIdx) =>
          <.div(^.className:="column", ^.key:="col-"+colIdx,
            column.zipWithIndex.map { (cell, rowIdx) =>
              <.div(^.className:=s"cell ${cell.cssClasses}", ^.key:=colIdx+"-"+rowIdx,
                ^.onKeyUp ==> ((ev: SyntheticKeyboardEvent[_]) => onKeyUp(ev)),
                cell.show
              )
            }.toVdomArray
          )
        }.toVdomArray
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(Board.zero)
    .useEffectOnMountBy { (props, state) => IO.delay {
      //TODO test change-propagation, cleanup listener
      dom.window.addEventListener("keydown", (ev:SyntheticKeyboardEvent[_]) => {
        console.log("key pressed", ev)
      })
      }
    }
    .render(renderFn)
}
