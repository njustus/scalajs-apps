package com.github.njustus.sjsapps.shared

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
  
  given Conversion[(Int, Int), Coordinate] with {
    override def apply(x: (Int, Int)): Coordinate = Coordinate(x._1, x._2)
  }
}
