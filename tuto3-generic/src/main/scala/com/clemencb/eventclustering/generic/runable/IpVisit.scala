package com.clemencb.eventclustering.generic.runable

import java.sql.Timestamp

import com.clemencb.eventclustering.generic.session.{Event, Identifiable}

/**
  * Case class of the event "user X established a connexion with IP Y and time T"
  */
case class IpVisit(ip: String, override val user: String, override val time: Timestamp) extends Event with Identifiable
