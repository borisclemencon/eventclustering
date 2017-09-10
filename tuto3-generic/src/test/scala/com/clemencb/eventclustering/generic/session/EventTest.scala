package com.clemencb.eventclustering.generic.session

import java.sql.Timestamp

import com.clemencb.eventclustering.generic.runable.WebsiteVisit
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by borisclemencon on 10/09/2016.
  */
class EventTest extends FlatSpec with Matchers {
  val event1 = WebsiteVisit("www.google.com", "toto", Timestamp.valueOf("2017-09-09 21:15:01.000"))
  val event2 = WebsiteVisit("www.amazon.com", "toto", Timestamp.valueOf("2017-09-09 21:15:02.000"))
  val event3 = WebsiteVisit("www.fnac.com", "toto", Timestamp.valueOf("2017-09-09 21:15:03.000"))

  "sort()" should "reorder (ascending) a list of unordered events" in {
    val session = Session(List(event2,event1,event3))
    assert( session.events.map( _.site )==List("www.google.com", "www.amazon.com", "www.fnac.com"), "Unordered events in session" )
  }

  it should "not change and ordered list" in {
    val session = Session(List(event1,event2,event3))
    assert( session.events.map( _.site )==List("www.google.com", "www.amazon.com", "www.fnac.com"), "Unordered events in session" )
  }

  "Operator <" should "return true if left hand side event is before right hand side event" in {
    assert( event1 < event2 , "Operator < should return true" )
    assert( event2 < event3 , "Operator < should return true" )
    assert( event1 < event3 , "Operator < should return true" )
  }

  it should "return false left hand side event is after right hand side event" in {
    assert( !(event2 < event1) , "Operator < should return false" )
  }

  "Operator >" should "return true if right hand side event is before left hand side event" in {
    assert( event2 > event1 , "Operator > should return true" )
    assert( event3 > event2 , "Operator > should return true" )
    assert( event3 > event1 , "Operator > should return true" )
  }

  it should "return false left hand side event is after right hand side event" in {
    assert( !(event1 > event2) , "Operator > should return false" )
  }

}
