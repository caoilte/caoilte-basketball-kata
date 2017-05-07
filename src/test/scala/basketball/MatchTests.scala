package basketball

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class MatchTests extends FlatSpec with Matchers {

  behavior.of("MatchEvent parseDataEvent")

  it should "parse a bit encoded match event in correctly" in {
    MatchEvent.parseDataEvent(0x781002) shouldEqual MatchEvent(2, 0, 2, 0, 15)
  }

  behavior.of("MatchEvent isValid")

  it should "accept valid scores" in {
    MatchEvent(1, 0, 4, 0, 15).isValid shouldEqual true
    MatchEvent(2, 0, 4, 0, 15).isValid shouldEqual true
    MatchEvent(3, 0, 4, 0, 15).isValid shouldEqual true
  }

  it should "reject invalid scores" in {
    MatchEvent(0, 0, 4, 0, 15).isValid shouldEqual false
  }

  behavior.of("MatchEvent isValidEventAfter")

  it should "recognise a correct next MatchEvent as valid if Team 1 scored" in {
    MatchEvent(2, 0, 4, 0, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual true
  }
  it should "recognise a correct next MatchEvent as valid if Team 2 scored" in {
    MatchEvent(2, 1, 2, 2, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual true
  }

  it should "recognise an incorrect next MatchEvent as invalid if Team 1 scoring doesn't update Team 1 score correctly" in {
    MatchEvent(2, 0, 2, 0, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual false
  }
  it should "recognise an incorrect next MatchEvent as invalid if Team 2 scoring doesn't update Team 2 score correctly" in {
    MatchEvent(2, 1, 2, 0, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual false
  }

  it should "recognise an incorrect next MatchEvent as valid if Team 1 scores and both team scores are higher than expected (ie some events were skipped)" in {
    MatchEvent(2, 0, 100, 100, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual true
  }
  it should "recognise an incorrect next MatchEvent as valid if Team 2 scores and both team scores are higher than expected (ie some events were skipped)" in {
    MatchEvent(2, 1, 100, 100, 15).isValidEventAfter(MatchEvent(2, 0, 2, 0, 10)) shouldEqual true
  }

  behavior.of("MatchEvent fixedToGoAfter")

  behavior.of("MatchState lastEventBefore")

  it should "find no last event when there is none" in {
    val state = new MatchState
    state.lastEventBefore(MatchEvent(2, 0, 2, 0, 15)) shouldEqual None
  }

  it should "find the last event before when it is the last event chronologically" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))

    state.lastEventBefore(MatchEvent(1, 1, 2, 3, 30)) shouldEqual Option(MatchEvent(2, 1, 2, 2, 28))
  }

  it should "find the last event before when it is not the last event chronologically" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 10, 10, 55))

    state.lastEventBefore(MatchEvent(1, 1, 2, 3, 30)) shouldEqual Option(MatchEvent(2, 1, 2, 2, 28))
  }

  behavior.of("MatchState lastEventOption")

  it should "find no last event when there is none" in {
    val state = new MatchState

    state.lastEventOption shouldEqual None
  }

  it should "find the last event when there is one" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))

    state.lastEventOption shouldEqual Option(MatchEvent(2, 1, 2, 2, 28))
  }

  behavior.of("MatchState lastEvent")

  it should "find the last event" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))

    state.lastEvent() shouldEqual MatchEvent(2, 1, 2, 2, 28)
  }

  behavior.of("MatchState lastEvents")

  it should "return zero events if requested" in {
    val state = new MatchState
    state.lastEvents(0) shouldEqual Vector()
  }

  it should "return the requested number of events reverse chronologically" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 10, 10, 55))

    state.lastEvents(2) shouldEqual Vector(
      MatchEvent(3, 1, 10, 10, 55),
      MatchEvent(2, 1, 2, 2, 28)
    )
  }

  it should "throw an IllegalArgumentException if more than the number of events are requested" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))

    val caught = intercept[IllegalArgumentException] {
      state.lastEvents(2)
    }
    caught.getMessage shouldEqual "Asked for 2 events but there are only 1"
  }

  behavior.of("MatchState allEvents")

  it should "return all events successfully added so far reverse chronologically" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))

    state.lastEvents(2) shouldEqual Vector(
      MatchEvent(2, 1, 2, 2, 28),
      MatchEvent(2, 0, 2, 0, 15)
    )
  }

  behavior.of("MatchState addEvent")

  it should "reject an event that is invalid" in {
    val state = new MatchState
    state.addEvent(MatchEvent(0, 0, 2, 0, 15)) shouldEqual Left("MatchEvent(0,0,2,0,15) is invalid")

    state.allEvents shouldEqual Vector()
  }

  it should "reject an event that has the same elapsedMatchTimeInSeconds as another" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 2, 5, 28)) shouldEqual Left(
      "MatchEvent(3,1,2,5,28) has the same elapsedMatchTimeInSeconds as MatchEvent(2,1,2,2,28)"
    )

    state.allEvents shouldEqual Vector(
      MatchEvent(2, 1, 2, 2, 28),
      MatchEvent(2, 0, 2, 0, 15)
    )
  }

  it should "accept an event that demonstrably arrived too early" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 10, 10, 55))

    state.allEvents shouldEqual Vector(
      MatchEvent(3, 1, 10, 10, 55),
      MatchEvent(2, 1, 2, 2, 28),
      MatchEvent(2, 0, 2, 0, 15)
    )
  }

  it should "correctly insert an event that arrived out of order" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 10, 10, 55))
    state.addEvent(MatchEvent(1, 1, 2, 3, 30))

    state.allEvents shouldEqual Vector(
      MatchEvent(3, 1, 10, 10, 55),
      MatchEvent(1, 1, 2, 3, 30),
      MatchEvent(2, 1, 2, 2, 28),
      MatchEvent(2, 0, 2, 0, 15)
    )
  }

  it should "fix an event that can be made consistent with the state" in {
    val state = new MatchState
    state.addEvent(MatchEvent(2, 0, 2, 0, 15))
    state.addEvent(MatchEvent(2, 1, 2, 2, 28))
    state.addEvent(MatchEvent(3, 1, 2, 2, 33)) shouldEqual Right(MatchEvent(3, 1, 2, 5, 33))

    state.allEvents shouldEqual Vector(
      MatchEvent(3, 1, 2, 5, 33),
      MatchEvent(2, 1, 2, 2, 28),
      MatchEvent(2, 0, 2, 0, 15)
    )
  }
}
