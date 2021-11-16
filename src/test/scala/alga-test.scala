package alga

import org.scalacheck.*

object GraphProperties
  extends Properties("Graph"):
  
  import Gen.*
  import Prop.forAll

  type G = Graph[Int]

  given graphGen: Gen[G] =
    lzy(frequency(2 -> emptyGen, 5 -> vertexGen, 3 -> overlayGen, 2 -> connectGen))

  given tuple2GraphGen: Gen[(G, G)] =
    for { a <- graphGen ; b <- graphGen } yield (a, b)

  given tuple3GraphGen: Gen[(G, G, G)] =
    for { a <- graphGen ; b <- graphGen ; c <- graphGen } yield (a, b, c)

  given emptyGen: Gen[G] =
    const(Graph.empty[Int])

  given vertexGen: Gen[G] =
    posNum[Int].map(Graph.vertex)

  given overlayGen: Gen[G] =
    tuple2GraphGen.map(Graph.overlay)

  given connectGen: Gen[G] =
    tuple2GraphGen.map(Graph.connect)

  /* algebraic properties of alga.Graph */

  property("overlay is commutative") = forAll(tuple2GraphGen) {
    (a: G, b: G) => a + b === (b + a)
  }
  
  property("overlay is associative") = forAll(tuple3GraphGen) {
    (a: G, b: G, c: G) => (a + b) + c === (a + (b + c))
  }

  property("connect is associative") = forAll(tuple3GraphGen) {
    (a: G, b: G, c: G) => (a * b) * c === (a * (b * c))
  }

  property("connect has empty as left-identity") = forAll(graphGen) {
      (a: G) => Graph.empty * a === a
  }

  property("connect has empty as right-identity") = forAll(graphGen) {
    (a: G) => a * Graph.empty === a
  }

  property("connect distributes over left-overlay") = forAll(tuple3GraphGen) {
    (a: G, b: G, c: G) => (a + b) * c === ((a * c) + (b * c))
  }

  property("connect distributes over right-overlay") = forAll(tuple3GraphGen) {
    (a: G, b: G, c: G) => a * (b + c) === ((a * b) + (a * c))
  }

  property("connect decomposes") = forAll(tuple3GraphGen) {
    (a: G, b: G, c: G) => a * b * c === ((a * b) + (a * c) + (b * c))
  }
