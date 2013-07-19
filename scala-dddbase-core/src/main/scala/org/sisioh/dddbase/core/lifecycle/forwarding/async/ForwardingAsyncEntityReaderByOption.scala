package org.sisioh.dddbase.core.lifecycle.forwarding.async

import org.sisioh.dddbase.core.lifecycle.async.{AsyncEntityReadableByOption, AsyncEntityReader}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future

/**
 * [[org.sisioh.dddbase.core.lifecycle.async.AsyncEntityReadableByOption]]のデコレータ実装。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait ForwardingAsyncEntityReaderByOption[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityReadableByOption[ID, E] {
  this: AsyncEntityReader[ID, E] =>

  protected val delegateAsyncEntityReaderByOption: AsyncEntityReadableByOption[ID, E]

  def resolveOption(identity: ID): Future[Option[E]] =
    delegateAsyncEntityReaderByOption.resolveOption(identity)

}
