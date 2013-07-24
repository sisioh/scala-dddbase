package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.ResultWithEntity
import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter]]のデリゲート。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityWriter[ID, E] {

  type This <: ForwardingSyncEntityWriter[ID, E]

  type Delegate <: SyncEntityWriter[ID, E]

  /**
   * デリゲート。
   */
  protected val delegate: Delegate

  /**
   * 新しいインスタンスを作成する。
   *
   * @param state 新しい状態のデリゲートとエンティティ
   * @return 新しいインスタンスとエンティティ
   */
  protected def createInstance(state: Try[(Delegate#This, Option[E])]): Try[(This, Option[E])]

  def store(entity: E): Try[ResultWithEntity[This, ID, E, Try]] = {
    createInstance(
      delegate.store(entity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

  def delete(identity: ID): Try[This] = {
    createInstance(
      delegate.delete(identity).map {
        e =>
          (e.asInstanceOf[Delegate#This], None)
      }
    ).map(e => e._1)
  }

}
