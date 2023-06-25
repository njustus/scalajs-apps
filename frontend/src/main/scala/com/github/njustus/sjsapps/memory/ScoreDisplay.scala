package com.github.njustus.sjsapps.memory

import com.github.njustus.sjsapps.util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object ScoreDisplay {
  case class Props(memoryState: MemoryState)

  private def displayPlayer(currentPlayerId: String)(player:MemoryState.Player): VdomNode =
    val activeStyle = if(player.name == currentPlayerId) "player-active" else ""
    <.div(^.className:=s"column is-full ${player.cssClass}",
      <.h2(^.className:=activeStyle,s"Player: ${player.name}"),
      <.h4(s"Score: ${player.points}")
    )

  def renderFn(props: Props): VdomNode = {
    val dispPlayer = displayPlayer(props.memoryState.currentPlayersId)
    val players = props.memoryState.players.map(dispPlayer)
    <.div(^.className:="pt-4 columns is-multiline",
      players.toVdomArray
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
