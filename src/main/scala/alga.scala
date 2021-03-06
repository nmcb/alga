package alga

enum Graph[A]:
  case Empty                                 extends Graph[Nothing]
  case Vertex(a: A)                          extends Graph[A]
  case Overlay(lhs: Graph[A], rhs: Graph[A]) extends Graph[A]
  case Connect(lhs: Graph[A], rhs: Graph[A]) extends Graph[A]

  def +(that: Graph[A]): Graph[A] =
    Overlay(this, that)

  def *(that: Graph[A]): Graph[A] =
    Connect(this, that)

  def foldg[B](e: B, v: A => B, o: (B, B) => B, c: (B, B) => B): B =
    this match
      case Empty         => e
      case Vertex(a)     => v(a)
      case Overlay(l, r) => o(l.foldg(e, v, o, c), r.foldg(e, v, o, c))
      case Connect(l, r) => c(l.foldg(e, v, o, c), r.foldg(e, v, o, c))

  def map[B](f: A => B): Graph[B] =
    foldg(Graph.empty, f.andThen(Vertex.apply), Overlay.apply, Connect.apply)

  def flatMap[B](f: A => Graph[B]): Graph[B] =
    foldg(Graph.empty, f, Overlay.apply, Connect.apply)

  def isEmpty: Boolean =
    foldg(true, _ => false, _ && _, _ && _)

  def size: Int =
    foldg(1, _ => 1, _ + _, _ + _)

  def vertexSet: Set[A] =
    foldg(Set.empty, Set(_), _ ++ _, _ ++ _)

  def edgeSet: Set[(A, A)] =
    this match
      case Empty         => Set.empty
      case Vertex(_)     => Set.empty
      case Overlay(l, r) => l.edgeSet ++ r.edgeSet
      case Connect(l, r) => l.edgeSet ++ r.edgeSet ++ l.vertexSet.flatMap(a => r.vertexSet.map(b => (a, b)))

object Graph:

  def empty[A]: Graph[A] =
    Empty.asInstanceOf[Graph[A]]

  def vertex[A](a: A): Graph[A] =
    Vertex(a)

  def overlay[A](lhs: Graph[A], rhs: Graph[A]): Graph[A] =
    Overlay(lhs, rhs)

  def connect[A](lhs: Graph[A], rhs: Graph[A]): Graph[A] =
    Connect(lhs, rhs)
    
  import fpa.equality._

  given equality[A]: Eq[Graph[A]] =
    new Eq[Graph[A]]:
      def normalized(l: Graph[A])(r: Graph[A]): Boolean =
        l.vertexSet == r.vertexSet && l.edgeSet == r.edgeSet

  import fpa.numeric._

  given numeric[A](using num: Num[A]): Num[Graph[A]] =
    new Num[Graph[A]]:
      def plus(l: Graph[A])(r: Graph[A]): Graph[A] =
        Overlay(l, r)
      def star(l: Graph[A])(r: Graph[A]): Graph[A] =
        Connect(l, r)
      def pzero: Graph[A] =
        Graph.empty
      def szero: Graph[A] =
        Vertex(num.szero)
