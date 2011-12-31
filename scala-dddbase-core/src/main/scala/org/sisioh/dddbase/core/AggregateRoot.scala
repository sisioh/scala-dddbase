/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.dddbase.core

import event.DomainEventSeq
import java.io.{ ObjectOutputStream, IOException, ObjectInputStream }
import java.util.UUID

/**
 * DDDの集約パターンの集約ルートを表すトレイト。
 *
 * "集約ルートは通常はエンティティだが、複雑な内部構造を持つ値オブジェクトのこともあれば列挙された値のこともある。"
 *
 * @author j5ik2o
 */
trait AggregateRoot[ID <: UUID] extends Serializable {
  this: Entity[UUID] =>

  @transient
  private var eventBuilder = DomainEventSeq.newBuilder(this.identifier)

  @transient
  private var lastCommitted: Long = _

  /**
   * イベントを登録する。
   *  @param evnt [[org.sisioh.dddbase.core.DomainEvent]]
   */
  protected def registerEvent(event: DomainEvent): Unit = eventBuilder += event

  protected def initializeEventStream(lastSequenceNumber: Long) {
    eventBuilder.initializeSequenceNumber(lastSequenceNumber)
    lastCommitted = if (lastSequenceNumber >= 0) lastSequenceNumber else 0
  }

  /**
   * コミットされていないイベントをコミットする。
   */
  def commitEvents {
    lastCommitted = eventBuilder.result.lastSequenceNumber
    eventBuilder.clear
  }

  /**
   * コミットされていないイベントの[[org.sisioh.dddbase.core.event.DomainEventIterator]]を返す。
   *  @return [[org.sisioh.dddbase.core.event.DomainEventIterator]]
   */
  def uncommittedEvents = eventBuilder.result.iterator

  /**
   * アグリゲートのバージョンを返す。
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