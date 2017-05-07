package basketball

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class RealDataTests extends FlatSpec with Matchers {

  val sample1 = List[Int](
    0x801002, 0xf81016, 0x1d8102f, 0x248202a, 0x2e0203e, 0x348204e, 0x3b8384b, 0x468385e, 0x5304059, 0x640406e,
    0x6d8506a, 0x760606a, 0x838607e, 0x8e8707a, 0x930708e, 0x9f0709e, 0xad070a5, 0xb7880a2, 0xbf880b6,
    0xc9080c6, 0xd2090c2, 0xdd090d6, 0xed0a8d3, 0xf98a8e6, 0x10a8b8e2, 0x1178b8ed, 0x1228c8ea, 0x12b0d8ea
  )

  val sample2 = List[Int](
    0x781002, 0xe01016, 0x1081014, 0x1e0102f, 0x258202a, 0x308203e, 0x388204e, 0x388204e, 0x3d0384b, 0x478385e,
    0x618406e, 0x5404059, 0x6b8506a, 0x750706c, 0x7d8507e, 0x938608e, 0x8b8607a, 0xa10609e, 0xb8870a2,
    0xc4870b6, 0xcc070c6, 0x2ee74753, 0xd5080c2, 0xdf080d6, 0xe4098d3, 0xec098f6, 0xfc8a8e2, 0x10a8a8ed,
    0x1180b8ea, 0x1218c8ea
  )

  "sample1" should "parse correctly" in {
    val matchState = new MatchState
    sample1.map(MatchEvent.parseDataEvent).foreach(matchState.addEvent)

    matchState.allEvents.size shouldEqual sample1.size
  }

  "sample2" should "parse correctly" in {
    val matchState = new MatchState
    sample2.map(MatchEvent.parseDataEvent).foreach(matchState.addEvent)

    matchState.allEvents.size shouldEqual 27
  }

  "sample2 errors" should "match our assumptions" in {
    val matchState = new MatchState
    val addResults: Seq[Either[String, MatchEvent]] =
      sample2.map(MatchEvent.parseDataEvent).map(matchState.addEvent)

    println(addResults)

    addResults.flatMap(_.left.toOption) shouldEqual List(
      "MatchEvent(0,1,2,2,33) is invalid",
      "MatchEvent(2,1,4,9,113) has the same elapsedMatchTimeInSeconds as MatchEvent(2,1,4,9,113)",
      "MatchEvent(0,1,14,13,234) is invalid"
    )
  }

  "sample2 pretty printed and reversed" should "read correctly and match the specification" in {
    val matchState = new MatchState
    sample2.map(MatchEvent.parseDataEvent).foreach(matchState.addEvent)

    matchState.allEvents.reverse.map(_.prettyString) shouldEqual Vector(
      "At 00:15, a 2-point shot for Team 1 gives them a 2 point lead at 2-0",
      "At 00:28, a 2-point shot for Team 2 levels the game for Team 2 at 2 points each",
      "At 01:00, a 3-point shot for Team 2 gives them a 3 point lead at 2-5",
      "At 01:15, a 2-point shot for Team 1 leaves them 1 point behind at 4-5",
      "At 01:37, a 2-point shot for Team 2 gives them a 3 point lead at 4-7",
      "At 01:53, a 2-point shot for Team 2 gives them a 5 point lead at 4-9",
      "At 02:02, a 3-point shot for Team 1 leaves them 2 points behind at 7-9",
      "At 02:23, a 2-point shot for Team 2 gives them a 4 point lead at 7-11",
      "At 02:48, a single point for Team 1 leaves them 3 points behind at 8-11",
      "At 03:15, a 2-point shot for Team 2 gives them a 6 point lead at 7-13",
      "At 03:35, a 2-point shot for Team 1 leaves them 4 points behind at 9-13",
      "At 04:11, a 2-point shot for Team 2 gives them a 6 point lead at 9-15",
      "At 04:39, a 2-point shot for Team 1 leaves them 4 points behind at 11-15",
      "At 04:55, a 2-point shot for Team 2 gives them a 8 point lead at 9-17",
      "At 05:22, a 2-point shot for Team 2 gives them a 10 point lead at 9-19",
      "At 06:09, a 2-point shot for Team 1 leaves them 6 points behind at 14-20",
      "At 06:33, a 2-point shot for Team 2 gives them a 8 point lead at 14-22",
      "At 06:48, a 2-point shot for Team 2 gives them a 10 point lead at 14-24",
      "At 07:06, a 2-point shot for Team 1 leaves them 8 points behind at 16-24",
      "At 07:26, a 2-point shot for Team 2 gives them a 10 point lead at 16-26",
      "At 07:36, a 3-point shot for Team 1 leaves them 7 points behind at 19-26",
      "At 07:52, a 2-point shot for Team 2 gives them a 9 point lead at 19-28",
      "At 08:25, a 2-point shot for Team 1 leaves them 7 points behind at 21-28",
      "At 08:53, a single point for Team 2 gives them a 8 point lead at 21-29",
      "At 09:20, a 2-point shot for Team 1 leaves them 6 points behind at 23-29",
      "At 09:39, a 2-point shot for Team 1 leaves them 4 points behind at 25-29",
      "At 25:00, a 3-point shot for Team 1 leaves them 2 points behind at 232-234"
    )
  }

}
