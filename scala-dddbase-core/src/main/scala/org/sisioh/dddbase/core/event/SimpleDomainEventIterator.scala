package org.sisioh.dddbase.core.event

import org.sisioh.dddbase.core.DomainEvent

class SimpleDomainEventIterator(private val iterator: Iterator[DomainEvent])
  extends DomainEventIterator {

  @volatile
  private var peekedOption: Option[DomainEvent] = None

  def this(events: List[DomainEvent]) = this(events.iterator)

  def this(events: DomainEvent*) = this(events.iterator)

  def peek: DomainEvent = peekedOption match {
    case Some(peeked) => peeked
    case None => {
      val result = iterator.next
      peekedOption = Some(result)
      result
    }
  }

  def next: DomainEvent = peekedOption match {
    case Some(peeked) => {
      val result = peeked
      peekedOption = None
      result
    }
    case None => iterator.next
  }

  def hasNext: Boolean = peekedOption != None || iterator.hasNext

}