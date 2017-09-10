package com.clemencb.eventclustering.generic.runable

import java.sql.Timestamp

import com.clemencb.eventclustering.generic.session.{Event, Identifiable}

/**
  * Case class of the event "user X visited the website Y and time T"
  */
case class WebsiteVisit(site: String, override val user: String, override val time: Timestamp) extends Event with Identifiable
