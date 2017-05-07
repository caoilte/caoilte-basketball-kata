package basketball

import java.util.concurrent.ConcurrentSkipListMap

import utils._

case class MatchEvent(pointsScored: Int,
                      whoScored: Int,
                      team1PointsTotal: Int,
                      team2PointsTotal: Int,
                      elapsedMatchTimeInSeconds: Int) {

  def isValid: Boolean =
    pointsScored > 0

  def isValidEventAfter(previousMatchEvent: MatchEvent): Boolean = {
    val scoringPlayersTotalIncreasesCorrectly = if (whoScored == 0) {
      pointsScored + previousMatchEvent.team1PointsTotal == team1PointsTotal
    } else pointsScored + previousMatchEvent.team2PointsTotal == team2PointsTotal

    val nonScoringPlayerScoreStaysTheSame = if (whoScored == 0) {
      previousMatchEvent.team2PointsTotal == team2PointsTotal
    } else previousMatchEvent.team1PointsTotal == team1PointsTotal

    (scoringPlayersTotalIncreasesCorrectly && nonScoringPlayerScoreStaysTheSame) ||
    (!scoringPlayersTotalIncreasesCorrectly && !nonScoringPlayerScoreStaysTheSame)
  }

  def fixedToGoAfter(previousMatchEvent: MatchEvent): MatchEvent = {
    require(
      elapsedMatchTimeInSeconds >= previousMatchEvent.elapsedMatchTimeInSeconds,
      s"(BUG) This event $toString does not follow $previousMatchEvent"
    )

    val newTeam1PointsTotal = if (whoScored == 0) {
      previousMatchEvent.team1PointsTotal + pointsScored
    } else previousMatchEvent.team1PointsTotal

    val newTeam2PointsTotal = if (whoScored == 1) {
      previousMatchEvent.team2PointsTotal + pointsScored
    } else previousMatchEvent.team2PointsTotal

    copy(
      team1PointsTotal = newTeam1PointsTotal,
      team2PointsTotal = newTeam2PointsTotal
    )
  }
}

object MatchEvent {
  private val pointsScored              = 0 to 1 asBitPatternIntParser
  private val whoScored                 = 2 asBitPatternIntParser
  private val team2PointsTotal          = 3 to 10 asBitPatternIntParser
  private val team1PointsTotal          = 11 to 18 asBitPatternIntParser
  private val elapsedMatchTimeInSeconds = 19 to 30 asBitPatternIntParser

  def parseDataEvent(dataEvent: Int): MatchEvent =
    MatchEvent(
      pointsScored = pointsScored(dataEvent),
      whoScored = whoScored(dataEvent),
      team1PointsTotal = team1PointsTotal(dataEvent),
      team2PointsTotal = team2PointsTotal(dataEvent),
      elapsedMatchTimeInSeconds = elapsedMatchTimeInSeconds(dataEvent)
    )

}

class MatchState {
  import scala.collection.JavaConverters._
  private val state: ConcurrentSkipListMap[Int, MatchEvent] =
    new ConcurrentSkipListMap[Int, MatchEvent]

  /**
    * We use the negative of timeElapsed for our key because,
    * - we want to take views of the map that are latest first (ie highest elapsed time)
    * - "Ascending key ordered views and their iterators are faster than descending ones."
    *
    * See: http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentSkipListMap.html#values()
    */
  private def keyFor(matchEvent: MatchEvent): Int = -matchEvent.elapsedMatchTimeInSeconds

  def lastEventBefore(matchEvent: MatchEvent): Option[MatchEvent] =
    if (state.isEmpty) None
    else Option(state.ceilingEntry(keyFor(matchEvent)).getValue)

  def lastEventOption: Option[MatchEvent] =
    if (state.isEmpty) None else Option(state.firstEntry().getValue)
  def lastEvent(): MatchEvent = state.firstEntry().getValue

  private def putToState(eventToPut: MatchEvent): MatchEvent = {
    val _ = state.put(keyFor(eventToPut), eventToPut)
    eventToPut
  }

  def addEvent(event: MatchEvent): Either[String, MatchEvent] =
    if (event.isValid) {
      lastEventBefore(event) match {
        case Some(lastEvent) =>
          if (event.elapsedMatchTimeInSeconds == lastEvent.elapsedMatchTimeInSeconds) {
            Left(s"$event has the same elapsedMatchTimeInSeconds as $lastEvent")
          } else if (event.isValidEventAfter(lastEvent)) {
            Right(putToState(event))
          } else {
            val fixedEvent = event.fixedToGoAfter(lastEvent)
            Right(putToState(fixedEvent))
          }
        case None => Right(putToState(event))
      }
    } else Left(s"$event is invalid")

  def lastEvents(num: Int): IndexedSeq[MatchEvent] = {
    val res = state.values().asScala.take(num).toIndexedSeq
    if (res.size != num)
      throw new IllegalArgumentException(s"Asked for $num events but there are only ${res.size}")
    else res
  }

  def allEvents: IndexedSeq[MatchEvent] = state.values().asScala.toIndexedSeq

}
