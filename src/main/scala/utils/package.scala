import scala.annotation.tailrec

package object utils {
  implicit class IntOps(i: Int) {

    @tailrec
    final def **(power: Int): Int =
      if (power == 0) 1
      else if (power == 1) i
      else i * 2 ** (power - 1)

    def asBitPatternIntParser: BitPatternIntParser =
      (i to i).asBitPatternIntParser
  }

  implicit class RangeOps(r: Range) {
    private val mask: Int = ((2 ** r.length) - 1) << r.start

    def asBitMaskString                            = mask.toBinaryString
    def asBitPatternIntParser: BitPatternIntParser = new BitPatternIntParser(mask, r.start)
  }
}

package utils {
  class BitPatternIntParser(mask: Int, offset: Int) extends (Int => Int) {
    override def apply(toParse: Int): Int = (toParse & mask) >> offset
  }
}
