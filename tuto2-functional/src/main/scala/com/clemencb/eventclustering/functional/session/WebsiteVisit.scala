package com.clemencb.eventclustering.functional.session

import java.sql.Timestamp

/**
  * Case class of the event "user X visited the website Y and time T"
  */
private[functional] case class WebsiteVisit(site: String, user: String, time: Timestamp) extends Ordered[WebsiteVisit] with Serializable {
  override def compare(that: WebsiteVisit): Int = {
    if (time.before(that.time)) -1
    else if (time.equals(that.time)) 0
    else +1
  }
}