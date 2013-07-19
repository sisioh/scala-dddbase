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

  /**
   * デリゲート。
   */
  protected val delegateEntityWriter: SyncEntityWriter[ID, E]

  /**
   * 新しいインスタンスを作成する。
   *
   * @param state 新しい状態のデリゲートとエンティティ
   * @return 新しいインスタンスとエンティティ
   */
  protected def createInstance(state: Try[(SyncEntityWriter[ID, E]#This, Option[E])]): Try[(This, Option[E])]

  def store(entity: E): Try[ResultWithEntity[This, ID, E, Try]] = {
    createInstance(
      delegateEntityWriter.store(entity).map {
        e =>
          (e.result, Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

  def delete(identity: ID): Try[This] = {
    createInstance(
      delegateEntityWriter.delete(identity).map {
        e =>
          (e, None)
      }
    ).map(e => e._1)
  }

}
