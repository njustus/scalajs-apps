package com.github.njustus.sjsapps.tetris

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object TetrisBoard {

  case class Props(board: List[List[TetrisCell]])
  type State = Unit

  private def renderFn(props: Props): VdomNode = {
    props.board.zipWithIndex.map { (row, rowIdx) =>
      <.div(^.className := "flex", ^.key := "row-"+rowIdx,
        row.zipWithIndex.map { (cell, colIdx) =>
          <.div(^.className := "flex cell", ^.key := "col-"+colIdx)
        }.toVdomArray
      )
    }.toVdomArray
  }

  def render(board: List[List[TetrisCell]]): VdomNode = component(Props(board))

  private val component = ScalaFnComponent.withHooks[Props]
    .render(renderFn)
}
