/*
 * Copyright 2011-2013 Sisioh Project and others. (http://www.sisioh.org/)
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
package org.sisioh.dddbase.lifecycle.memory.async

import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.async.AsyncResultWithEntity
import org.sisioh.dddbase.core.model.{ Identifier, EntityCloneable, Entity }
import scala.concurrent._

/**
 * 非同期型オンメモリ不変リポジトリの骨格実装を提供するためのトレイト。
 *
 * `Future` の中で `core` に同期型のオンメモリリポジトリを利用することで非同期版として
 * 実装を提供する。リポジトリの状態変更を起こすメソッドを呼び出した際に、新しいインスタンス
 * を生成するか `this` を返すかは `createInstance` メソッドの振る舞いによって決定する。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepositoryOnMemorySupport[ID <: Identifier[_], E <: Entity[ID]]
    extends AsyncRepositoryOnMemory[ID, E] {

  protected def createInstance(entities: Map[ID, E]): This

  override def equals(obj: Any) = obj match {
    case that: AsyncRepositoryOnMemorySupport[_, _] =>
      this.entities == that.entities
    case _ => false
  }

  override def hashCode = 31 * entities.hashCode()

  override def resolveBy(identifier: ID)(implicit ctx: Ctx) = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      entities(identifier)
    }.recoverWith {
      case ex: NoSuchElementException =>
        Future.failed(new EntityNotFoundException(Some(s"identifier = $identifier")))
    }
  }

  override def store(entity: E)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      val result = createInstance(entities + (entity.identifier -> entity))
      AsyncResultWithEntity[This, ID, E](result.asInstanceOf[This], entity)
    }
  }

  override def existBy(identifier: ID)(implicit ctx: Ctx): Future[Boolean] = {
    implicit val executor = getExecutionContext(ctx)
    Future {
      entities.contains(identifier)
    }
  }

  override def deleteBy(identifier: ID)(implicit ctx: Ctx): Future[Result] = {
    implicit val executor = getExecutionContext(ctx)
    resolveBy(identifier).map {
      entity =>
        val result = createInstance(entities - identifier)
        AsyncResultWithEntity[This, ID, E](result.asInstanceOf[This], entity)
    }
  }

}
