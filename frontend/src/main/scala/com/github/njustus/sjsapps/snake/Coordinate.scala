package com.github.njustus.sjsapps.snake

import cats.kernel.Monoid

case class Coordinate(x: Int, y: Int) {
  def isAt(otherX: Int, otherY: Int): Boolean =
    x == otherX && otherY == y
}

object Coordinate {
  given Monoid[Coordinate] with {
    override def empty: Coordinate = Coordinate(0, 0)
    override def combine(c1: Coordinate, c2: Coordinate): Coordinate =
      Coordinate(c1.x + c2.x, c1.y + c2.y)
  }
}
