/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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

import java.util.UUID

/**
 * 識別子を表すトレイト。
 *
 * @author j5ik2o
 */
trait Identifier extends Serializable {
  /**文字列表現としての識別子を取得する。
   * @return 文字列表現としての識別子
   */
  def asString: String
}

/**[[java.lang.String]]を使った[[org.sisioh.dddbase.core.Identifier]]の実装クラス。
 *
 * @author j5ik2o
 * @param value [[java.lang.String]]
 */
class StringIdentifier(val value: String) extends Identifier {

  def asString = value.toString

}

/**[[java.util.UUID]]を使った[[org.sisioh.dddbase.core.Identifier]]の実装クラス。
 *
 * @author j5ik2o
 * @param value [[java.util.UUID]]
 */
class UUIDIdentifier(val value: UUID) extends Identifier {

  override def equals(that: Any): Boolean = that match {
    case other: UUIDIdentifier => value == other.value
    case _ => false
  }

  override def hashCode = value.hashCode

  override def toString = "Identifier(%s)".format(value)

  def asString = value.toString

}

/**
 * コンパニオンオブジェクト。
 *
 * @author j5ik2o
 */
object UUIDIdentifier {

  /**[[java.util.UUID]]を基に、新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(value: UUID): UUIDIdentifier = new UUIDIdentifier(value)

  /**文字列形式のUUIDを新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(value: String): UUIDIdentifier = apply(UUID.fromString(value))

  /**文字列を基に、新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def from(value: String): UUIDIdentifier = apply(UUID.nameUUIDFromBytes(value.getBytes))

  /**[[java.util.UUID]]を自動生成し、新しいインスタンスを生成する。
   *
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(): UUIDIdentifier = new UUIDIdentifier(UUID.randomUUID)

  /**[[org.sisioh.dddbase.core.UUIDIdentifier]]のための抽出子メソッド。
   *
   * @param entityIdentifier [[org.sisioh.dddbase.core.UUIDIdentifier]]
   * @return [[org.sisioh.dddbase.core.UUIDIdentifier]]のvalue
   */
  def unapply(entityIdentifier: UUIDIdentifier) = Some(entityIdentifier.value)
}

