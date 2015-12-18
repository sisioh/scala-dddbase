/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
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
package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.{ Identifier, Entity }
import scala.concurrent._

/**
 * 非同期版`org.sisioh.dddbase.core.lifecycle.Repository`。
 *
 * @see `org.sisioh.dddbase.core.lifecycle.Repository`
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncRepository[ID <: Identifier[_], E <: Entity[ID]]
    extends Repository[ID, E, Future]
    with AsyncEntityReader[ID, E]
    with AsyncEntityWriter[ID, E] {

  type This <: AsyncRepository[ID, E]

}

