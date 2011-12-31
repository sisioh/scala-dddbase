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
package org.sisioh.dddbase.core

import scalaz.Identity


/**
 * [[org.sisioh.dddbase.core.Entity]]を説明するメタデータ。
 *
 * <p>イミュータブルでなければならない(must)。</p>
 *
 * @tparam T 実体のモデル型
 * @tparam ID 識別子の型
 * @author j5ik2o
 */
trait EntityMeta[T <: Entity[ID], ID <: java.io.Serializable] {

  /**
   * 参照先要素の同一性を調べる。
   *
   * @param obj 比較対象
   * @return 同じIDの要素を参照している場合は{ @code true}、そうでない場合は{ @code false}
   */
  def equals(obj: Any): Boolean

  /**
   * 実体を特定する識別子として[[scalaz.Identity]]を取得する。
   *
   * @return [[scalaz.Identity]]
   */
  val identifier: Identity[ID]

  /**
   * この参照オブジェクトが引数{@code target}の参照かどうか調べる。
   *
   * <p>引数の型である[[org.sisioh.dddbase.core.Entity]]型は、
   * T型であるとよりタイプセーフとなるが、
   * [[org.sisioh.dddbase.core.Entity]]型の抽象度で、
   * 「この参照の実体である」ことがチェックできると
   * 有効なケースがあるため、敢えて[[org.sisioh.dddbase.core.Entity]]型としている。
   * ただし、偶然同じIDを持ってしまったが型が異なる{@link Entity}が存在してしまった場合は
   * その後の処理で`ClassCastException`が発生する可能性があるが、
   * [[java.util.UUID]] の衝突率は無視できるほど小さい。</p>
   *
   * @param target 対象{ @link Entity}
   * @return この参照オブジェクトが引数{ @code target}の参照の場合は{ @code true}、そうでない場合は{ @code false}
   */
  def isReferenceOf(target: Entity[ID]): Boolean

}