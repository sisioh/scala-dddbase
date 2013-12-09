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
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を読み込むための責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait EntityReader[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityIO {

  def resolve(identity: ID)(implicit ctx: EntityIOContext[M]): M[E]

  /**
   * 複数の値からエンティティを取得し、`M[Seq[E]]` に変換する。
   *
   * @param values 入力値の集合
   * @param f エンティティを引き当てるための関数
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @tparam V 入力値の型
   * @return M にラップされた Seq[E]
   */
  protected def traverse[V](values: Seq[V])(f: (V) => M[E])
                           (implicit ctx: EntityIOContext[M]): M[Seq[E]]

  def resolves(identities: Seq[ID])(implicit ctx: EntityIOContext[M]): M[Seq[E]]

  def apply(identity: ID)(implicit ctx: EntityIOContext[M]): M[E] = resolve(identity)

  def containsByIdentity(identity: ID)(implicit ctx: EntityIOContext[M]): M[Boolean]

  def containsByIdentities(identities: Seq[ID])(implicit ctx: EntityIOContext[M]): M[Boolean]

  def contains(entity: E)(implicit ctx: EntityIOContext[M]): M[Boolean] = containsByIdentity(entity.identity)

  def contains(entities: Seq[E])(implicit ctx: EntityIOContext[M]): M[Boolean] = containsByIdentities(entities.map(_.identity))

}

