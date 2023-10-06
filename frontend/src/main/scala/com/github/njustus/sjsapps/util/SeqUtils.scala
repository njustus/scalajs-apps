package com.github.njustus.sjsapps.util

object SeqUtils {
  def getIdx[A](idx: Int)(xs: Seq[A]): Option[A] =
    if (idx < 0 || idx >= xs.length) None
    else Some(xs(idx))

  def allEqual[A](xs:Seq[A]): Boolean =
    if (xs.size <= 1) true
    else xs.tail.forall(tl => tl == xs.head)
}
