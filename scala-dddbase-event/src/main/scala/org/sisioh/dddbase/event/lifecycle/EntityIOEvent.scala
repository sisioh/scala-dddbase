package org.sisioh.dddbase.event.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import org.sisioh.dddbase.event.DomainEvent

/**
 * [[org.sisioh.dddbase.core.model.Entity]]をI/Oしたイベントを表すクラス。
 *
 * @param identity このイベントの識別子。
 * @param entity エンティティ。
 * @param eventType [[org.sisioh.dddbase.event.lifecycle.EventType.Value]]
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
class EntityIOEvent[ID <: Identity[_], E <: Entity[_]]
(val identity: ID, val entity: E, val eventType: EventType.Value)
  extends DomainEvent[ID]
