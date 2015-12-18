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

import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.language.higherKinds

/**
 * 述語関数に該当したエンティティを検索することができるトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait EntityReadableAsPredicate[ID <: Identifier[_], E <: Entity[ID], M[+A]] {
  this: EntityReader[ID, E, M] =>

  /**
   * 述語関数に該当したエンティティを取得する。
   *
   * @param predicate 述語関数
   * @param index チャンクのインデックス
   * @param maxEntities 1チャンク内の件数
   * @return モナドにラップした`org.sisioh.dddbase.core.lifecycle.EntitiesChunk`
   */
  def filterBy(predicate: E => Boolean, index: Option[Int] = None, maxEntities: Option[Int] = None)(implicit ctx: Ctx): M[EntitiesChunk[ID, E]]

}
