package object basketball {

  implicit class MatchEventPrettyPrinter(matchEvent: MatchEvent) {
    import matchEvent._

    private def theNameOfTheScore: String =
      if (pointsScored == 1) "a single point"
      else if (pointsScored == 2) "a 2-point shot"
      else if (pointsScored == 3) "a 3-point shot"
      else "an invalid shot"

    private def pointsDifference(a: Int, b: Int, pluralize: Boolean): String = {
      val difference = Math.abs(a - b)
      if (difference == 1) "1 point"
      else if (pluralize) s"${difference} points"
      else s"${difference} point"
    }

    private def theTeamWhoScored =
      if (whoScored == 0) "Team 1"
      else "Team 2"

    private def leavesTheSituation =
      if (team1PointsTotal == team2PointsTotal)
        s"levels the game for $theTeamWhoScored"
      else if ((team1PointsTotal > team2PointsTotal && whoScored == 0) ||
               (team2PointsTotal > team1PointsTotal && whoScored == 1))
        s"gives them a ${pointsDifference(team1PointsTotal, team2PointsTotal, false)} lead"
      else if ((team1PointsTotal > team2PointsTotal && whoScored == 1) ||
               (team2PointsTotal > team1PointsTotal && whoScored == 0))
        s"leaves them ${pointsDifference(team1PointsTotal, team2PointsTotal, true)} behind"
      else throw new Exception(s"Unexpected points combination for ${super.toString}")

    private def timeOfEvent =
      f"${elapsedMatchTimeInSeconds / 60}%02d" + ":" + f"${elapsedMatchTimeInSeconds % 60}%02d"

    private def score =
      if (team1PointsTotal == team2PointsTotal)
        s"${pointsDifference(team1PointsTotal, 0, true)} each"
      else
        s"$team1PointsTotal-$team2PointsTotal"

    def prettyString: String =
      s"At $timeOfEvent, $theNameOfTheScore for $theTeamWhoScored $leavesTheSituation at $score"
  }

}
