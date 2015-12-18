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

import org.sisioh.dddbase.core.lifecycle.ResultWithEntities
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.util.Try

trait SyncResultWithEntities[+R <: SyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]]
  extends ResultWithEntities[R, ID, T, Try]

object SyncResultWithEntities {

  def apply[R <: SyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]](result: R, entities: Seq[T]): SyncResultWithEntities[R, ID, T] =
    SyncResultWithEntitiesImpl(result, entities)

  def unapply[R <: SyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]](target: SyncResultWithEntities[R, ID, T]): Option[(R, Seq[T])] = Some(target.result, target.entities)

}

private[sync] case class SyncResultWithEntitiesImpl[+R <: SyncEntityWriter[ID, T], ID <: Identifier[_], T <: Entity[ID]](result: R, entities: Seq[T])
  extends SyncResultWithEntities[R, ID, T]

