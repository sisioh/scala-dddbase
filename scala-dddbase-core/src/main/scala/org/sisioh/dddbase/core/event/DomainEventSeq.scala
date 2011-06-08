package org.sisioh.dddbase.core.event

import scala.collection._
import generic.CanBuildFrom
import mutable.{ Builder, ListBuffer }
import org.sisioh.dddbase.core.Identifier
import org.sisioh.dddbase.core.DomainEvent
import org.sisioh.dddbase.core.UUIDIdentifier

/** [[DomainEvent]]を格納するコンテナ。
 *
 *  @author j5ik2o
 */
@serializable
class DomainEventSeq private[event] (val aggregateId: Identifier,
  val lastSequenceNumber: Long,
  private val source: Seq[DomainEvent])
  extends Seq[DomainEvent] with SeqLike[DomainEvent, DomainEventSeq] {

  import mutable.Builder

  private val eventBuffer = ListBuffer.empty[DomainEvent]
  eventBuffer ++= source

  def iterator: DomainEventIterator = new SimpleDomainEventIterator(eventBuffer.toList)

  def length: Int = eventBuffer.length

  def apply(idx: Int): DomainEvent = eventBuffer(idx)

  override protected[this] def newBuilder: Builder[DomainEvent, DomainEventSeq] =
    DomainEventSeq.newBuilder(aggregateId, lastSequenceNumber)

}

/** [[DomainEventSeq]]のためのビルダー。
 *
 *  @author j5ik2o
 */
class DomainEventSeqBuilder(val aggregateId: Identifier = UUIDIdentifier(),
  private var firstSequenceNumber: Long = 0)
  extends Builder[DomainEvent, DomainEventSeq] {

  private var lastSequenceNumberOption: Option[Long] = None

  private val builder = new ListBuffer[DomainEvent]

  def lastSequenceNumber = lastSequenceNumberOption match {
    case Some(value) => value
    case None => newSequenceNumber
  }

  def initializeSequenceNumber(lastKnownSequenceNumber: Long) {
    require(builder.size == 0, "Cannot set first sequence number if events have already been added")
    firstSequenceNumber = if (lastSequenceNumberOption == None) 0L else lastKnownSequenceNumber + 1
    println("firstSequenceNumber = " + firstSequenceNumber)
    lastSequenceNumberOption = Some(lastKnownSequenceNumber)
  }

  private def newSequenceNumber = {
    lastSequenceNumberOption match {
      case Some(value) => lastSequenceNumberOption = Some(value + 1)
      case None => lastSequenceNumberOption = Some(firstSequenceNumber)
    }
    lastSequenceNumberOption.get
  }

  def +=(element: DomainEvent): this.type = {
    require(aggregateId == element.aggregateId,
      "aggregateId = %s, element.aggregateId = %s".format(aggregateId, element.aggregateId))
    element.sequenceNumberOption match {
      case Some(_) => lastSequenceNumberOption = element.sequenceNumberOption
      case None => element.sequenceNumberOption = Some(newSequenceNumber)
    }
    builder += element
    this
  }

  def result = new DomainEventSeq(aggregateId, lastSequenceNumber, builder.result)

  def clear = builder.clear
}

/** [[DomainEventContainer]]のコンパニオンオブジェクト。
 *
 *  @author j5ik2o
 */
object DomainEventSeq {

  type From = Seq[DomainEvent]
  type Elem = DomainEvent
  type To = DomainEventSeq

  def apply(aggregateId: Identifier, source: Seq[DomainEvent]): DomainEventSeq = {
    val builder = newBuilder(aggregateId)
    builder ++= source
    builder.result
  }

  def apply(source: Seq[DomainEvent]): DomainEventSeq = {
    val builder = newBuilder
    builder ++= source
    builder.result
  }

  def apply(aggregateId: Identifier): DomainEventSeq = {
    val builder = newBuilder(aggregateId)
    builder.result
  }

  def apply(): DomainEventSeq = {
    val builder = newBuilder
    builder.result
  }

  implicit def canBuildFrom: CanBuildFrom[From, Elem, To] =
    new CanBuildFrom[From, Elem, To] {

      def apply(from: From) = {
        from match {
          case dec: DomainEventSeq => newBuilder(dec.aggregateId)
          case _ => throw new Error
        }
      }

      def apply() = newBuilder

    }

  /** 新しいビルダーを生成する。
   *
   *  @return 新しいビルダー
   */
  def newBuilder: DomainEventSeqBuilder = new DomainEventSeqBuilder

  /** 新しいビルダーを生成する。
   *
   *  @param aggregateId 集約の識別子
   *  @return 新しいビルダー
   */
  def newBuilder(aggregateId: Identifier): DomainEventSeqBuilder =
    new DomainEventSeqBuilder(aggregateId)

  def newBuilder(aggregateId: Identifier, firstSequenceNumber: Long): DomainEventSeqBuilder =
    new DomainEventSeqBuilder(aggregateId, firstSequenceNumber)

}