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

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * エンティティを`Option`でラップして返すための[[org.sisioh.dddbase.core.lifecycle.EntityReader]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait EntityReadableByOption[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  this: EntityReader[ID, E, M] =>

  /**
   * 識別子に該当するエンティティを解決する。
   *
   * @see [[org.sisioh.dddbase.core.lifecycle.EntityReader]] `resolve`
   *
   * @param identity 識別子
   * @return Mと`Option`でラップされたエンティティ。エンティティがない場合はNoneとなる。
   */
  def resolveOption(identity: ID)(implicit ctx: EntityIOContext[M]): M[Option[E]]

}
