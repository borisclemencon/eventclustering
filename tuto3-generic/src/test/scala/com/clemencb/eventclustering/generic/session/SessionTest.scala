package com.clemencb.eventclustering.generic.session

import java.sql.Timestamp
import java.time.Duration

import com.clemencb.eventclustering.generic.runable.WebsiteVisit
import org.scalatest.{Matchers, WordSpec}


/**
  * Created by borisclemencon on 09/09/2016.
  */
class SessionTest extends WordSpec with Matchers {

  "A session of WebsiteVisits" when {

    "made of only one event" should {
      "return one only sequence of 1 event" in {
        val session = Session(List(
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000"))
        ))
        val listOfSessions = session.clusterize(Duration.ofSeconds(2))
        assert(listOfSessions.size == 1, "There should be one only session")
        assert(listOfSessions.head.events.size == 1, "There should be one only event in the session")
        assert(listOfSessions.head.events.head.site == "www.amazon.com", "The only event of the session should be A")
      }
    }

    "made of n events every seconds" should {
      "return one only sequence of n events if threshold is 2 seconds " in {
        val session = Session(List(
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000")),
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:02.000"))
        ))
        val listOfSessions = session.clusterize(Duration.ofSeconds(2))
        assert(listOfSessions.size == 1, "There should be two session")
      }

      "return 1 sequence of 10 events" in {
        val session = Session((0 until 10).map(i => WebsiteVisit("www.google.com", "toto", Timestamp.valueOf(s"2017-09-09 21:15:0$i.000"))).toList)
        val listOfSessions = session.clusterize(Duration.ofSeconds(2))
        assert(listOfSessions.size == 1, "There should be one session of 10 events")
      }
    }

    "made of n events every seconds" should {
      "return n sequences of 1 event if threshold is 0.5 seconds " in {
        val session = Session(List(
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000")),
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:02.000"))
        ))
        val listOfSessions = session.clusterize(Duration.ofMillis(500))
        assert(listOfSessions.size == 2, "There should be two session")
        assert(listOfSessions.map(sessions => sessions.events.size).forall(_ == 1), "both sessions should have only one event")
      }

      "return 10 session of 1 event if threshold is 2 seconds" in {
        val session = Session((1 to 10).map(i => WebsiteVisit("www.google.com", "toto", Timestamp.valueOf(s"2017-09-09 21:15:0$i.000"))).toList)
        val listOfSessions = session.clusterize(Duration.ofSeconds(2))
        assert(listOfSessions.size == 1, "There should be one session of 10 events")
      }
    }

    "made of 2 pairs of 2 events" should {
      "return 2 sequences of 2 event" in {
        val session = Session(List(
          WebsiteVisit("www.google.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000")),
          WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:02.000")),

          WebsiteVisit("www.amazon.com", "toto",
            Timestamp.valueOf("2017-09-09 21:15:05.000")),
          WebsiteVisit("www.fnac.com", "toto", Timestamp.valueOf("2017-09-09 21:15:06.000"))
        ))
        val listOfSessions = session.clusterize(Duration.ofSeconds(2))
        assert(listOfSessions.size == 2, "There should be two session")
        assert(listOfSessions.map(sessions => sessions.events.size).forall(_ == 2), "Both sessions should have two events")
      }
    }
  }
}
