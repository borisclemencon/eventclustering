package com.clemencb.eventclustering.naive.session

import java.time.Duration


/**
  * Ordered list of website visit [[WebsiteVisit]]
  *
  * @param listOfEvents the un-ordered list of event (sort is ran in constructor)
  */
private[naive] class Session(listOfEvents: List[WebsiteVisit]) {

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
    if (listOfEvents.isEmpty) List.empty[Session]
    else {
      val output = new scala.collection.mutable.ListBuffer[Session]
      var curList = new scala.collection.mutable.ListBuffer[WebsiteVisit]
      val it = listOfEvents.toIterator
      var e1 = it.next()
      curList.append(e1)

      while (it.hasNext) {
        val e2 = it.next()
        val delta = Duration.ofMillis(e2.time.toInstant.toEpochMilli - e1.time.toInstant.toEpochMilli)
        if (delta.compareTo(threshold) > 0) {
          output.append(new Session(curList.toList))
          curList = new scala.collection.mutable.ListBuffer[WebsiteVisit]
        }
        curList.append(e2)
        e1=e2
      }
      output.append(new Session(curList.toList))
      output.toList
    }
  }
}
