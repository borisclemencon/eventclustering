package com.clemencb.eventclustering.generic.session

import java.time.Duration

import scala.annotation.tailrec


/**
  * Class for a ordered list of events (implementing trait Event)
  * @param listOfEvents the list of events
  * @tparam E any case class implementing the trait Event
  */
private[generic] class Session[E <: Event](listOfEvents: List[E]) {

  // events must be sorted!
  val events: List[E] = listOfEvents.sortBy(_.time.getTime())

  /**
    * Time series clustering.
    *
    * @param threshold the threshold beyond which two events are not in the same sequence
    * @return the list of event sequence
    */
  def clusterize(threshold: Duration): List[Session[E]] = {
    @tailrec
    def clusterRec(events: List[E], current: List[E], savedSequences: List[Session[E]]): List[Session[E]] = {
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
private[generic] object Session {

  def apply[E <: Event](events: List[E]): Session[E] = {
    new Session(events)
  }

  def apply[E <: Event](event1: E, event2: E): Session[E] = {
    new Session(List(event1, event2))
  }

  def apply[E <: Event](event1: E, event2: E, event3: E): Session[E] = {
    new Session(List(event1, event2, event3))
  }

}

