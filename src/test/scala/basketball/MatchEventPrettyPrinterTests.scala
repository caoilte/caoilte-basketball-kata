package basketball

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class MatchEventPrettyPrinterTests extends FlatSpec with Matchers {

  behavior.of("MatchEvent Pretty Printer")

  it should "show team 1 scoring in a single point leading position correctly" in {
    MatchEvent(1, 0, 1, 0, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
  it should "show team 2 scoring in a single point leading position correctly" in {
    MatchEvent(1, 1, 0, 1, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 2 gives them a 1 point lead at 0-1"
  }
  it should "show team 1 scoring in a multi point leading position correctly" in {
    MatchEvent(1, 0, 2, 0, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 gives them a 2 point lead at 2-0"
  }
  it should "show team 2 scoring in a multi point leading position correctly" in {
    MatchEvent(1, 1, 0, 2, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 2 gives them a 2 point lead at 0-2"
  }

  it should "show team 1 scoring in a single point trailing position correctly" in {
    MatchEvent(1, 0, 1, 2, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 leaves them 1 point behind at 1-2"
  }
  it should "show team 2 scoring in a single point trailing position correctly" in {
    MatchEvent(1, 1, 2, 1, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 2 leaves them 1 point behind at 2-1"
  }
  it should "show team 1 scoring in a multi point trailing position correctly" in {
    MatchEvent(1, 0, 1, 3, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 leaves them 2 points behind at 1-3"
  }
  it should "show team 2 scoring in a multi point trailing position correctly" in {
    MatchEvent(1, 1, 3, 1, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 2 leaves them 2 points behind at 3-1"
  }

  it should "show team 1 scoring to draw level correctly" in {
    MatchEvent(1, 0, 1, 1, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 levels the game for Team 1 at 1 point each"
  }
  it should "show team 2 scoring to draw level correctly" in {
    MatchEvent(1, 1, 1, 1, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 2 levels the game for Team 2 at 1 point each"
  }

  it should "show one point being scored correctly" in {
    MatchEvent(1, 0, 1, 0, 10).prettyString shouldEqual
      "At 00:10, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
  it should "show two points being scored correctly" in {
    MatchEvent(2, 0, 2, 0, 10).prettyString shouldEqual
      "At 00:10, a 2-point shot for Team 1 gives them a 2 point lead at 2-0"
  }
  it should "show three points being scored correctly" in {
    MatchEvent(3, 0, 3, 0, 10).prettyString shouldEqual
      "At 00:10, a 3-point shot for Team 1 gives them a 3 point lead at 3-0"
  }

  it should "show a single digit second time correctly" in {
    MatchEvent(1, 0, 1, 0, 1).prettyString shouldEqual
      "At 00:01, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
  it should "show a double digit second time correctly" in {
    MatchEvent(1, 0, 1, 0, 59).prettyString shouldEqual
      "At 00:59, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
  it should "show a single digit minute time correctly" in {
    MatchEvent(1, 0, 1, 0, 60).prettyString shouldEqual
      "At 01:00, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
  it should "show a double digit minute time correctly" in {
    MatchEvent(1, 0, 1, 0, 1345).prettyString shouldEqual
      "At 22:25, a single point for Team 1 gives them a 1 point lead at 1-0"
  }
}
