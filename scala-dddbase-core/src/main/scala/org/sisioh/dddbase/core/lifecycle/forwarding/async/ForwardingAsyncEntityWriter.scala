package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.{AsyncResultWithEntity, AsyncEntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityWriter]]のデコレータ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityWriter[ID, E] {

  type Delegate <: AsyncEntityWriter[ID, E]

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
  protected def createInstance(state: Future[(Delegate#This, Option[E])]): Future[(This, Option[E])]

  def store(entity: E)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    val state = delegate.store(entity).map {
      result =>
        (result.result.asInstanceOf[Delegate#This], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        AsyncResultWithEntity(e._1, e._2.get)
    }
  }

  def deleteByIdentity(identity: ID)(implicit ctx: EntityIOContext[Future]): Future[AsyncResultWithEntity[This, ID, E]] = {
    implicit val executor = getExecutionContext(ctx)
    val state = delegate.deleteByIdentity(identity).map {
      result =>
        (result.result.asInstanceOf[Delegate#This], Some(result.entity))
    }
    val instance = createInstance(state)
    instance.map {
      e =>
        AsyncResultWithEntity(e._1, e._2.get)
    }
  }

}
