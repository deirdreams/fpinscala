package laziness

import org.scalatest._

class StreamSpec extends FlatSpec with Matchers {
  "toList" should "return a list from a stream" in {
    val s = Stream(1, 2, 3)
    s.toList should be (List(1, 2, 3))
  }

  "take" should "take the first n elements of a stream" in {
    val s = Stream(1, 2, 3, 4, 5)
    s.take(0) should be (empty)
    s.take(1) should be (Stream(1))
    s.take(3) should be (Stream(1, 2, 3))
    s.take(10) should be (s)
  }

  "drop" should "skip the first n elements of a stream" in {
    val s = Stream(1, 2, 3, 4, 5)
    s.drop(0) should be (s)
    s.drop(2) should be (Stream(3, 4, 5))
    s.drop(5) should be (empty)
  }

  "takeWhile" should "create a stream whose elements are from the original stream while the pred is true" in {
    def p = (x:Int) => x > 0
    def p1 = (x:Int) => x < 0
    val s = Stream(1, 4, -7, -9, 10)
    val s1 = Stream(-1, -2, 6, 5, -2, -4)
    s.takeWhile(p) should be (Stream(1, 4))
    s.takeWhile(p1) should be (Stream())
    s1.takeWhile(p) should be (Stream())
    s1.takeWhile(p1) should be (Stream(-1, -2))
  }

//  "forAll" should "return the correct boolean depending if the predicate evaluates true for all items" in {
//    def p = (x:Int) => x < 10
//    val s = Stream(1, 2, 4, 5, 9, 13, 19, 2)
//    val s1 = Stream(1, 2, 3, 9, 7, 8)
//    s.forAll(p) should be (false)
//    s1.forAll(p) should be (true)
//  }
}
