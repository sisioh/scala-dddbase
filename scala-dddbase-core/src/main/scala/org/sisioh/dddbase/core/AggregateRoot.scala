package org.sisioh.dddbase.core

import event.DomainEventSeq
import java.io.{ ObjectOutputStream, IOException, ObjectInputStream }

/** DDDの集約パターンの集約ルートを表すトレイト。
 *
 *  "集約ルートは通常はエンティティだが、複雑な内部構造を持つ値オブジェクトのこともあれば列挙された値のこともある。"
 *
 *  @author j5ik2o
 */
@serializable
trait AggregateRoot {
  this: Entity =>

  @transient
  private var eventBuilder = DomainEventSeq.newBuilder(identifier)

  @transient
  private var lastCommitted: Long = _

  /** イベントを登録する。
   *  @param evnt [[DomainEvent]]
   */
  protected def registerEvent(event: DomainEvent): Unit = eventBuilder += event

  protected def initializeEventStream(lastSequenceNumber: Long) {
    eventBuilder.initializeSequenceNumber(lastSequenceNumber)
    lastCommitted = if (lastSequenceNumber >= 0) lastSequenceNumber else 0
  }

  /** コミットされていないイベントをコミットする。
   */
  def commitEvents {
    lastCommitted = eventBuilder.result.lastSequenceNumber
    eventBuilder.clear
  }

  /** コミットされていないイベントのストリームを返す。
   *  @return Stream[DomainEvent]
   */
  def uncommittedEvents = eventBuilder.result.iterator

  /** アグリゲートのバージョンを返す。
   *  @return アグリゲートのバージョン
   */
  def version = lastCommitted

  @throws(classOf[IOException])
  @throws(classOf[ClassNotFoundException])
  private def readObject(in: ObjectInputStream) {
    in.defaultReadObject
    lastCommitted = in.readObject.asInstanceOf[Long]
    eventBuilder = DomainEventSeq.newBuilder(identifier)
    eventBuilder.initializeSequenceNumber(lastCommitted)
    val uncommitted = in.readObject.asInstanceOf[List[DomainEvent]]
    uncommitted.foreach { uncommittedEvent =>
      eventBuilder += uncommittedEvent
    }
  }

  @throws(classOf[IOException])
  private def writeObject(out: ObjectOutputStream) {
    out.defaultWriteObject
    out.writeObject(lastCommitted)
    out.writeObject(eventBuilder.result)
  }

}