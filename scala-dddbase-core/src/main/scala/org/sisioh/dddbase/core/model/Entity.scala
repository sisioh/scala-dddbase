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
package org.sisioh.dddbase.core.model

/**
 * エンティティを表すトレイト。
 *
 * @tparam ID 識別子の型
 */
trait Entity[ID <: Identifier[_]] {

  /** エンティティの識別子。 */
  val identifier: ID

  /**
   * ハッシュコードを返す。
   *
   * @return ハッシュコード
   */
  override final def hashCode: Int = 31 * identifier.##

  /**
   * 指定されたオブジェクトと等価であるかを判定する。
   *
   * @param that オブジェクト
   * @return 等価である場合はtrue
   */
  override final def equals(that: Any): Boolean = that match {
    case that: Entity[_] => identifier == that.identifier
    case _               => false
  }

}

