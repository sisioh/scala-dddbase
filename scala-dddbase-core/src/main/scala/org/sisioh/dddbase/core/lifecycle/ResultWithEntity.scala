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
package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Identity, Entity}
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]の新しい状態とエンティティを保持する値オブジェクト。
 *
 * @tparam EW [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]の型
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 * @tparam M モナドの型
 */
trait ResultWithEntity[+EW <: BaseEntityWriter[ID, E, M], ID <: Identity[_], E <: Entity[ID], M[+A]] {

  /**
   * 結果
   */
  val result: EW

  /**
   * エンティティ
   */
  val entity: E

}
