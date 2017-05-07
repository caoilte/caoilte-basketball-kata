package utils

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class UtilsTests extends FlatSpec with Matchers {

  behavior.of("int **")

  it should "should correctly calculate an exponent" in {
    2 ** 3 shouldEqual 8
  }

  behavior.of("range asBitMask")

  it should "return a single element range with no offsets as 1" in {
    (0 to 0 asBitMaskString) shouldEqual "1"
  }

  it should "return a two element range with no offest as 11" in {
    (0 to 1 asBitMaskString) shouldEqual "11"
  }

  it should "return a single element range with two offsets as 100" in {
    (2 to 2 asBitMaskString) shouldEqual "100"
  }

  it should "return a multi element range with multiple offsets correctly" in {
    (3 to 10 asBitMaskString) shouldEqual "11111111000"
  }

  behavior.of("BitPatternIntParser")

  it should "parse a two element range with 0 offset (points scored)" in {
    val p = 0 to 1 asBitPatternIntParser

    p(0x781002) shouldEqual 2
    p(0xf0101f) shouldEqual 3
    p(0x1310c8a1) shouldEqual 1
    p(0x29f981a2) shouldEqual 2
    p(0x48332327) shouldEqual 3
  }

  it should "parse a one element range with 2 offset (who scored)" in {
    val p = 2 to 2 asBitPatternIntParser

    p(0x781002) shouldEqual 0
    p(0xf0101f) shouldEqual 1
    p(0x1310c8a1) shouldEqual 0
    p(0x29f981a2) shouldEqual 0
    p(0x48332327) shouldEqual 1
  }

  it should "parse an eight element range with 3 offset (team 2 points total)" in {
    val p = 3 to 10 asBitPatternIntParser

    p(0x781002) shouldEqual 0
    p(0xf0101f) shouldEqual 3
    p(0x1310c8a1) shouldEqual 20
    p(0x29f981a2) shouldEqual 52
    p(0x48332327) shouldEqual 100
  }

  it should "parse an eight element range with 11 offset (team 1 points total)" in {
    val p = 11 to 18 asBitPatternIntParser

    p(0x781002) shouldEqual 2
    p(0xf0101f) shouldEqual 2
    p(0x1310c8a1) shouldEqual 25
    p(0x29f981a2) shouldEqual 48
    p(0x48332327) shouldEqual 100
  }

  it should "parse a twelve element range with 19 offset (elapsed match time)" in {
    val p = 19 to 30 asBitPatternIntParser

    p(0x781002) shouldEqual 15
    p(0xf0101f) shouldEqual 30
    p(0x1310c8a1) shouldEqual 610
    p(0x29f981a2) shouldEqual 1343
    p(0x48332327) shouldEqual 2310
  }
}
