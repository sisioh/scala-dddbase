package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.event.Event

/** ドメインイベントを表すトレイト。
 *
 *  @author j5ik2o
 */
trait DomainEvent extends Event with Entity {

  /** イベントの識別子。 */
  override val identifier: Identifier

  /** 集約ルートの識別子。 */
  val aggregateId: Identifier

  /** 順序。 */
  var sequenceNumberOption: Option[Long] = None

}

