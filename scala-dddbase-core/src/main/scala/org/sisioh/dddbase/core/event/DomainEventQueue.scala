package org.sisioh.dddbase.core.event

import scala.collection.mutable.Queue
import scala.language.implicitConversions

class DomainEventQueue {

  private var sequenceNumber = 0L

  private val queue = new Queue[DomainEvent] {

    override def +=(domainEvent: DomainEvent) = {
      sequenceNumber += 1;
      super.+=(domainEvent)
    }
    override def clear() {
      initializeSequenceNumber(0)
      super.clear()
    }
  }

  def initializeSequenceNumber(initial: Long) {
    sequenceNumber = initial
  }

  def lastSequenceNumber = sequenceNumber
}

object DomainEventQueue {

  implicit def toQueue(deq: DomainEventQueue): Queue[DomainEvent] = deq.queue

}