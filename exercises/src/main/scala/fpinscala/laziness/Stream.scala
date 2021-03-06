package fpinscala.laziness

import Stream._
trait Stream[+A] {

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean = 
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }

  def toList: List[A] = {
    @annotation.tailrec
    def rec(s: Stream[A], acc: List[A]): List[A] = s match {
      case Cons(x, xs) => rec(xs(), x() :: acc)
      case _ => acc
    }
    rec(this, List[A]()).reverse
  }

  def take(n: Int): Stream[A] = this match {
    case Cons(x, xs) if n > 1 => cons(x(), xs().take(n-1))
    case Cons(x, _) if n == 1 => cons(x(), empty)
    case _ => empty 
  }

  def drop(n: Int): Stream[A] = this match {
    case Cons(x, xs) if n > 0 => xs().drop(n-1)
    case _ => this
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(x, xs) if p(x()) => cons(x(), xs().takeWhile(p))
    case _ => empty
  }

  def forAll(p: A => Boolean): Boolean =
    this.foldRight(true)((x, xs) => p(x) && xs)

  def headOption: Option[A] = foldRight(None: Option[A])((x, _) => Some(x))

  // 5.7 map, filter, append, flatmap using foldRight. Part of the exercise is
  // writing your own function signatures.

  def startsWith[B](s: Stream[B]): Boolean = ???
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty 
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)
  def from(n: Int): Stream[Int] = unfold(n)(n => Some(n, n+1))

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z) match {
    case Some(x, xs) => cons(x, unfold(xs)(f))
    case None => empty
  }
}
