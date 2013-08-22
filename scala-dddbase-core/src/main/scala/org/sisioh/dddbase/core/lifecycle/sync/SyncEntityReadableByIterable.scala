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
package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.{Success, Try}
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader]]に
 * `scala.collection.Iterable`を実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncEntityReadableByIterable[ID <: Identity[_], E <: Entity[ID]] extends Iterable[E] {
  this: SyncEntityReader[ID, E] =>

  def containsByIdentity(identifier: ID)
                        (implicit ctx: EntityIOContext[Try]): Try[Boolean] = Success(exists(_.identity == identifier))

}
