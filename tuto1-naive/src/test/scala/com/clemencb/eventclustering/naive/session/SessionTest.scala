package com.clemencb.eventclustering.naive.session

import java.sql.Timestamp
import java.time.Duration

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by borisclemencon on 09/09/2016.
  */
class SessionTest extends FlatSpec with Matchers {

  "clusterize()" should "return 1 sessions of 1 event for one only event and threshold 2" in {
    val session = new Session(List(WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000"))))
    val listOfSessions = session.clusterize(Duration.ofSeconds(2))
    assert(listOfSessions.size == 1, "There should be one only session")
    assert(listOfSessions.head.events.size == 1, "There should be one only event in the session")
    assert(listOfSessions.head.events.head.site == "www.amazon.com", "The only event of the session should be A")
  }


  it should "return 2 sessions of 1 event for two events more distant in time then threshold 2" in {
    val session = new Session(List(
      WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000")),
      WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:10.000"))
    ))
    val listOfSessions = session.clusterize(Duration.ofSeconds(2))
    assert(listOfSessions.size == 2, "There should be two session")
    assert(listOfSessions.forall(_.events.size == 1), "both sessions should have only one event")
  }


  it should "return 2 sessions of 2 events for four events resp. at time (1,2, 5,6) and threshold 2" in {
    val session = new Session(List(
      WebsiteVisit("www.google.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000")),
      WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:02.000")),
      WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:05.000")),
      WebsiteVisit("www.fnac.com", "toto", Timestamp.valueOf("2017-09-09 21:15:06.000"))
    ))
    val listOfSessions = session.clusterize(Duration.ofSeconds(2))
    assert(listOfSessions.size == 2, "There should be two session")
    assert(listOfSessions.forall(_.events.size == 2), "Both sessions should have two events")
  }


  it should "return 1 session of 10 events for events at time (1,2,3,...,10) and threshold 2" in {
    val session = new Session((0 until 10).map(i => WebsiteVisit("www.google.com", "toto", Timestamp.valueOf(s"2017-09-09 21:15:0$i.000"))).toList)
    val listOfSessions = session.clusterize(Duration.ofSeconds(2))
    assert(listOfSessions.size == 1, "There should be one session of 10 events")
  }


  it should "return 1 session of 10 events and threshold 1" in {
    val session = new Session((1 to 10).map(i => WebsiteVisit("www.google.com", "toto", Timestamp.valueOf(s"2017-09-09 21:15:0$i.000"))).toList)
    val listOfSessions = session.clusterize(Duration.ofSeconds(2))
    assert(listOfSessions.size == 1, "There should be one session of 10 events")
  }

}
