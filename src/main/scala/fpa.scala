package fpa

object equality:
  trait Eq[A]:
    def normalized(l: A)(r: A): Boolean

  given universal[A]: Eq[A] =
    new Eq[A]:
      def normalized(l: A)(r: A) =
        l == r

  extension [A] (l: A) def ===(r: A)(using eq: Eq[A]): Boolean =
    eq.normalized(l)(r)

  extension [A] (l: A) def =/=(r: A)(using eq: Eq[A]) : Boolean =
    ! (l === r)

object numeric:
  trait Num[A]:
    def plus(l: A)(r: A): A
    def star(l: A)(r: A): A
    def pzero: A
    def szero: A

  given ints: Num[Int] =
    new Num[Int]:
      def plus(l: Int)(r: Int): Int = l + r
      def star(l: Int)(r: Int): Int = l * r
      def pzero: Int = 0
      def szero: Int = 1

  extension [A] (l: A) def |+|(r: A)(using num: Num[A]): A =
    num.plus(l)(r)

  def ε[A](using num: Num[A]): A =
    num.pzero

  extension [A] (l: A) def |*|(r: A)(using num: Num[A]): A =
    num.star(l)(r)

  def ú[A](using num: Num[A]): A =
    num.szero
