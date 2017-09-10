package com.clemencb.eventclustering.functional.session

import java.time.Duration

import scala.annotation.tailrec


/**
  * Class for a ordered list of website visits [[WebsiteVisit]]
  *
  * @param listOfEvents the list of events
  */
private[functional] class Session(listOfEvents: List[WebsiteVisit]) {

  // events must be sorted!
  val events: List[WebsiteVisit] = listOfEvents.sortBy(_.time.getTime())

  /**
    * Given the ordered series of events, group the events closer than the input threshold into sessions, and
    * return a list of sessions.
    *
    * @param threshold the threshold beyond which two events are not in the same sequence
    * @return the list of event sequence
    */
  def clusterize(threshold: Duration): List[Session] = {
    @tailrec
    def clusterRec(events: List[WebsiteVisit], current: List[WebsiteVisit], savedSequences: List[Session]): List[Session] = {
      events match {
        case Nil => savedSequences
        case first :: Nil => savedSequences :+ new Session(current :+ first)
        case first :: tail =>
          val delta = Duration.ofMillis(tail.head.time.toInstant.toEpochMilli - first.time.toInstant.toEpochMilli)
          if (delta.compareTo(threshold) > 0) clusterRec(tail, List(), savedSequences :+ new Session(current :+ first))
          else clusterRec(tail, current :+ first, savedSequences)
      }
    }

    clusterRec(events, List(), List())
  }

}

/** companion object */
private[functional] object Session {

  def apply(events: List[WebsiteVisit]): Session = {
    new Session(events)
  }

  def apply(event1: WebsiteVisit, event2: WebsiteVisit): Session = {
    new Session(List(event1, event2))
  }

  def apply(event1: WebsiteVisit, event2: WebsiteVisit, event3: WebsiteVisit): Session = {
    new Session(List(event1, event2, event3))
  }

}