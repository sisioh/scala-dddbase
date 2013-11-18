package org.sisioh.dddbase.core.lifecycle.forwarding.sync

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityWriter, SyncResultWithEntity}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityWriter]]のデリゲート。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingSyncEntityWriter[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityWriter[CTX, ID, E] {

  type This <: ForwardingSyncEntityWriter[CTX, ID, E]

  type Delegate <: SyncEntityWriter[CTX, ID, E]

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

  def store(entity: E)(implicit ctx: CTX): Try[SyncResultWithEntity[This, CTX, ID, E]] = {
    createInstance(
      delegate.store(entity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity[This, CTX, ID, E](e._1, e._2.get))
  }

  def deleteByIdentity(identity: ID)(implicit ctx: CTX): Try[SyncResultWithEntity[This, CTX, ID, E]] = {
    createInstance(
      delegate.deleteByIdentity(identity).map {
        e =>
          (e.result.asInstanceOf[Delegate#This], Some(e.entity))
      }
    ).map(e => SyncResultWithEntity[This, CTX, ID, E](e._1, e._2.get))
  }

}
