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
 * 基本的なリポジトリのトレイト。
 * リポジトリとして、基本的に必要な機能を定義するトレイト。
 *
 * リポジトリの状態を変更するメソッドの戻り値としては、
 * Immutableなリポジトリは新しいリポジトリインスタンスを返し、
 * Mutableなリポジトリは同一インスタンスを返すこと、を推奨する。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 */
trait Repository[ID <: Identity[_], T <: Entity[ID], M[+A]]
  extends EntityReader[ID, T, M] with EntityWriter[ID, T, M] {

  type R <: Repository[ID, T, M]

}

