package com.github.njustus.sjsapps

import com.github.njustus.sjsapps.capitalguessing.GuessingGameWrapper
import com.github.njustus.sjsapps.complete4.Complete4Board
import com.github.njustus.sjsapps.incomecalculator.IncomeCalculator
import com.github.njustus.sjsapps.kanbanboard.BoardComponent
import com.github.njustus.sjsapps.tippscalculator.TippsCalculator
import com.github.njustus.sjsapps.memory.Memory
import com.github.njustus.sjsapps.notes.NotesWrapper
import com.github.njustus.sjsapps.snake.{SnakeGame, SnakeGameWrapper}
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.*
import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.extra.router.{given, *}
import japgolly.scalajs.react.vdom.Implicits.*
import japgolly.scalajs.react.extra.router.Redirect
import japgolly.scalajs.react.extra.router.Redirect

object PageWrapper {
  case class Page(name: String, component: PageComponent) {
    val link = "/" + name.toLowerCase().replace(" ", "-")
  }

  case class Props(pages: List[Page]) {
    require(!pages.isEmpty, "page list can not be empty")

    private lazy val routerConfig = RouterConfigDsl[String].buildConfig { dsl =>
      import dsl.*

      val redirectHome = redirectToPage(pages.head.name)(SetRouteVia.HistoryPush)

      val rules = pages.foldLeft(emptyRule) { (acc, page) =>
        val route = staticRoute(page.link, page.name) ~> render(page.component())

        println(s"adding route ${page.link}")

        acc | route
      }

      rules.notFound(redirectHome)
    }

    lazy val router = Router(BaseUrl.fromWindowOrigin, routerConfig.logToConsole)
  }

  case class State(selectedKey: String)

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    val menuItems = props.pages.map { page =>
      <.li(^.key := page.name, <.a(^.href := page.link, page.name))
    }

    val router = props.router

    <.div(
      ^.className := "column is-full columns",
      <.aside(^.className := "column menu", <.ul(^.className := "menu-list", menuItems.toVdomArray)),
      <.div(^.className   := "column is-four-fifths", router())
    )

  }

  private val component = ScalaFnComponent
    .withHooks[Props]
    .useStateBy(props => State("Snake Game"))
    .render(renderFn)

  def build() = {
    val p = Props(
      List(
        Page("Tips Calculator", TippsCalculator.component),
        Page("Income Calculator", IncomeCalculator.component),
        Page("Memory", Memory.component),
        Page("Form", FormExample.component),
        Page("Kanban", BoardComponent.component),
        Page("Complete 4", Complete4Board.component),
        Page("Capital Game", GuessingGameWrapper.component),
        Page("Snake Game", SnakeGameWrapper.component),
        Page("Notes", NotesWrapper.component)
      )
    )

    component(p)
  }
}
