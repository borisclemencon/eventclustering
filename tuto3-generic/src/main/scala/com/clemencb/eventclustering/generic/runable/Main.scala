package com.clemencb.eventclustering.generic.runable

import java.io.{BufferedInputStream, FileInputStream}
import java.sql.Timestamp
import java.time.Duration
import java.util.zip.GZIPInputStream

import com.clemencb.eventclustering.generic.session.{Event, Identifiable, Session}

import scala.io.Source


/**
  * A task corresponding to reading and analuysis a dataset for main class
  * @param path the path of the input gzip csv file
  * @param decoder a function that take a csv string in input and return an subc-class of Event with Identifiable
  * @param threshold above which two events cannot belong to the same session
  * @tparam E an identifiable event
  */
case class ReadTask[E <: Event with Identifiable](path: String, decoder: String => E, threshold: Duration)



object Main extends App {

  /**
    * Process a line of file url.csv
    * @param sep csv separator symbol (',' by default)
    * @param s the string to parse
    * @return a [[WebsiteVisit]]
    */
  def processWebsiteLine(sep: String = ",")(s: String): WebsiteVisit = {
    val res = s.split(sep)
    val user = res(1).drop(1).dropRight(1)
    val site = res(2).drop(1).dropRight(1)
    val time = res(6)
    WebsiteVisit(site, user, Timestamp.valueOf(time))
  }

  /**
    * Process a line of file ip.csv
    * @param sep csv separator symbol (',' by default)
    * @param s the string to parse
    * @return a [[IpVisit]]
    */
  def processIpLine(sep: String = ",")(s: String): IpVisit = {
    val res = s.split(sep)
    val user = res(1).drop(1).dropRight(1)
    val ip = res(5).drop(1).dropRight(1)
    val time = res(4)
    IpVisit(ip, user, Timestamp.valueOf(time))
  }

  /**
    * Read the gzip csv file and apply to given decoder to parse each line
    * @param path csv separator symbol (',' by default)
    * @param decoder a function that take a csv string in input and return an subc-class of Event with Identifiable
    * @return a [[WebsiteVisit]]
    */
  def read[E <: Event with Identifiable](path: String, decoder: String => E): List[E] = {
    val is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(path)))
    Source.fromInputStream(is).
      getLines().
      drop(1). // drop header
      map(decoder).
      toList
  }

  /**
    * Print the n first lines of the dataset
    * @param data the dataset of E with Identifiable
    * @param n the number of line to print
    * @tparam E an identifiable event
    */
  def head[E <: Event with Identifiable](data: Iterable[E], n: Int = 10): Unit = {
    println(s"\n> HEAD OF DATASET")
    data.take(10).foreach {
      case WebsiteVisit(site, user, time) => println(s"  $user has seen website $site at $time")
      case IpVisit(ip, user, time) => println(s"  $user has establish a connexion to $ip at $time")
      case x => throw new IllegalArgumentException(s"Unhandled Event ${x.getClass} ")
    }
  }

  /**
    * Group by user and analyse sessions for each user
    * @param data the dataset of E with Identifiable
    * @param threshold above which two events cannot belong to the same session
    * @tparam E an identifiable event
    */
  def analyseSessions[E <: Event with Identifiable](data: Iterable[E], threshold: Duration): Unit = {
    println("\n> CLUSTERING ")
    val result = data.groupBy(_.user).mapValues(it => new Session(it.toList).clusterize(threshold))
    result.foreach(kv => {
      val user = kv._1
      val nbOfSessions = kv._2.size
      val avgNbOfVisit = kv._2.map(_.events.size).sum / nbOfSessions.toDouble
      println(f"  User ${kv._1} has done $nbOfSessions%4d sessions of $avgNbOfVisit%5.1f visits on average")
    })
  }

  // Application
  val tasks = List(
    ReadTask("./data/urls.csv.gz", processWebsiteLine(), Duration.ofHours(1)),
    ReadTask("./data/ips.csv.gz", processIpLine(), Duration.ofHours(1))
  )

  tasks.foreach(task => {
    println(s"\n###################### Dataset '${task.path}' ######################")
    val is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(task.path)))
    val data = read(task.path, task.decoder)
    head(data, 10)
    analyseSessions(data, task.threshold)
  })
}
