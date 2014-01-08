package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

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

  def storeEntity(entity: E)(implicit ctx: Ctx): Try[Result] = {
    createInstance(
      delegate.storeEntity(entity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

  def deleteByIdentifier(identity: ID)(implicit ctx: Ctx): Try[Result] = {
    createInstance(
      delegate.deleteByIdentifier(identity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity(e._1, e._2.get))
  }

}
