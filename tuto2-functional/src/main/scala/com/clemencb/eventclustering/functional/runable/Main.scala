package com.clemencb.eventclustering.functional.runable

import java.io.{BufferedInputStream, FileInputStream}
import java.sql.Timestamp
import java.time.Duration
import java.util.zip.GZIPInputStream

import com.clemencb.eventclustering.functional.session.{Session, WebsiteVisit}

import scala.io.Source

object Main extends App {

  def processCsvLine(sep: String = ",")(s: String): WebsiteVisit = {
    val res = s.split(sep)
    val user = res(1).drop(1).dropRight(1)
    val site = res(2).drop(1).dropRight(1)
    val time = res(6)
    WebsiteVisit(site, user, Timestamp.valueOf(time))
  }

  val path = "./data/urls.csv.gz"
  val is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(path)))

  val data = Source.
    fromInputStream(is).
    getLines().
    drop(1). // drop header
    map(processCsvLine()).
    toList

  // view 10 first events of data
  println("> HEAD OF DATASET ")
  data.take(10).foreach(x => println(s"  ${x.user} has seen website ${x.site} at ${x.time}"))

  // clusterize
  println("\n> CLUSTERING ")
  val result = data.groupBy(_.user).mapValues(new Session(_).clusterize(Duration.ofHours(1)))

  result.foreach(kv => {
    val nbOfSessions = kv._2.length
    val avgNbOfVisit = kv._2.map(_.events.size).sum / nbOfSessions.toDouble
    println(f"  User ${kv._1} has done $nbOfSessions%4d sessions of $avgNbOfVisit%5.1f visits on average")
  })
}
