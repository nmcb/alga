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
    foldg(Graph.empty, f.andThen(Vertex[B]), Overlay[B], Connect[B])

  def flatMap[B](f: A => Graph[B]): Graph[B] =
    foldg(Graph.empty, f, Overlay[B], Connect[B])

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

  def ===(that: Graph[A]): Boolean =
    this.vertexSet == that.vertexSet && this.edgeSet == that.edgeSet

  def =/=(that: Graph[A]): Boolean =
    ! ===(that)

object Graph:

  def empty[A]: Graph[A] =
    Empty.asInstanceOf[Graph[A]]

  def vertex[A](a: A): Graph[A] =
    Vertex(a)

  def overlay[A](lhs: Graph[A], rhs: Graph[A]): Graph[A] =
    Overlay(lhs, rhs)

  def connect[A](lhs: Graph[A], rhs: Graph[A]): Graph[A] =
    Connect(lhs, rhs)

  extension (i: Int) def node: Graph[Int] =
    Vertex(i)
