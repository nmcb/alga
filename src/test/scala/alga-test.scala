package alga

import org.scalacheck.*

object GraphProperties
  extends Properties("Graph"):
  
  import Gen.*

  type G = Graph[Int]

  given graphGen: Gen[G] =
    lzy(frequency(2 -> emptyGen, 5 -> vertexGen, 3 -> overlayGen, 2 -> connectGen))

  given tupple2GraphGen: Gen[(G, G)] =
    for { a <- graphGen ; b <- graphGen } yield (a, b)

  given tupple3GraphGen: Gen[(G, G, G)] =
    for { a <- graphGen ; b <- graphGen ; c <- graphGen } yield (a, b, c)

  given emptyGen: Gen[G] =
    const(Graph.empty[Int])

  given vertexGen: Gen[G] =
    Gen.posNum[Int].map(Graph.vertex)

  given overlayGen: Gen[G] =
    tupple2GraphGen.map(Graph.overlay)

  given connectGen: Gen[G] =
    tupple2GraphGen.map(Graph.connect)

  import Prop.forAll

  property("overlay is commutative") =
    forAll(tupple2GraphGen)
      ((a: G, b: G) => a + b === (b + a))
  

  property("overlay is associative") =
    forAll(tupple3GraphGen)
      ((a: G, b: G, c: G) => (a + b) + c === (a + (b + c)))

  property("connect is associative") =
    forAll(tupple3GraphGen)
      ((a: G, b: G, c: G) => (a * b) * c === (a * (b * c)))

  property("connect has empty as left-identity") =
    forAll(graphGen)
      ((a: G) => Graph.empty[Int] * a === a)

  property("connect has empty as right-identity") =
    forAll(graphGen)
      ((a: G) => a * Graph.empty[Int] === a)

  property("connect distributes over left-overlay") =
    forAll(tupple3GraphGen)
      ((a: G, b: G, c: G) => (a + b) * c === ((a * c) + (b * c)))

  property("connect distributes over right-overlay") =
    forAll(tupple3GraphGen)
      ((a: G, b: G, c: G) => a * (b + c) === ((a * b) + (a * c)))

  property("connect decomposes") =
    forAll(tupple3GraphGen)
      ((a: G, b: G, c: G) => a * b * c === ((a * b) + (a * c) + (b * c)))
