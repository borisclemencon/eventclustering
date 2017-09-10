package com.clemencb.eventclustering.naive.session

import java.sql.Timestamp

/**
  * Created by borisclemencon on 09/09/2016.
  */
private[naive] case class WebsiteVisit(site: String, user: String, time: Timestamp) extends Ordered[WebsiteVisit] with Serializable {
  override def compare(that: WebsiteVisit): Int = {
    if (time.before(that.time)) -1
    else if (time.equals(that.time)) 0
    else +1
  }
}