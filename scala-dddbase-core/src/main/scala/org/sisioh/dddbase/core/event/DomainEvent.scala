package org.sisioh.dddbase.core.event

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

import java.util.UUID
import scalaz.Identity
import org.sisioh.common.event.Event
import org.sisioh.dddbase.core.Entity

/**
 * ドメインイベントを表すトレイト。
 *
 * @author j5ik2o
 */
trait DomainEvent extends Event with Entity[UUID] {

  /** イベントの識別子。 */
  override val identity: Identity[UUID]

  /** 集約ルートの識別子。 */
  val aggregateIdentity: Identity[UUID]

  /** 順序。 */
  var sequenceNumberOption: Option[Long] = None

}

