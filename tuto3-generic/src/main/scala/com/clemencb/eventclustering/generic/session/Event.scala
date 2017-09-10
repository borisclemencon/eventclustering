package com.clemencb.eventclustering.generic.session

import java.sql.Timestamp

/**
  * Created by borisclemencon on 09/09/2016.
  */
private[generic] trait Event extends Ordered[Event] with Serializable {
  def time: Timestamp

  def compare(that: Event): Int = {
    if (time.before(that.time)) -1
    else if (time.equals(that.time)) 0
    else +1
  }
}

