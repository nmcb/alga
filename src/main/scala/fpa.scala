package fpa

object equality:
  trait Eq[A]:
    def normalized(l: A)(r: => A): Boolean

  given universal[A]: Eq[A] =
    new Eq[A]:
      inline def normalized(l: A)(r: => A) =
        l == r

  extension [A] (l: A) def ===(r: A)(using eq: Eq[A]): Boolean =
    eq.normalized(l)(r)

  extension [A] (l: A) def =/=(r: A)(using eq: Eq[A]) : Boolean =
    ! (l === r)
