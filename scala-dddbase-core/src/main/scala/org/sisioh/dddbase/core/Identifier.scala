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

trait Identifier {
  def asString: String
}

@serializable
class StringIdentifier(val value: String) extends Identifier {
  
	def asString = value.toString

}

/** エンティティの識別子を表すバリューオブジェクト。
 *
 *  @author j5ik2o
 *  @param value [[UUID]]
 */
@serializable
class UUIDIdentifier(val value: UUID) extends Identifier {

  override def equals(that: Any): Boolean = that match {
    case other: UUIDIdentifier => value == other.value
    case _ => false
  }

  override def hashCode = value.hashCode

  override def toString = "Identifier(%s)".format(value)

  def asString = value.toString

}

/** [[UUIDIdentifier]]のためのコンパニオンオブジェクト。
 *
 *  @author j5ik2o
 */
object UUIDIdentifier {

  /** [[UUID]]を基に、新しい[[Identifier]]のインスタンスを生成する。
   *
   *  @param value [[UUID]]
   *  @return 新しい[[UUIDIdentifier]]のインスタンス
   */
  def apply(value: UUID): UUIDIdentifier = new UUIDIdentifier(value)

  /** 文字列形式のUUIDを新しい[[Identifier]]のインスタンスを生成する。
   *
   *  @param value [[UUID]]
   *  @return 新しい[[Identifier]]のインスタンス
   */
  def apply(value: String): UUIDIdentifier = apply(UUID.fromString(value))

  /** 文字列を基に、新しい[[UUIDIdentifier]]のインスタンスを生成する。
   *
   *  @param value [[UUID]]
   *  @return 新しい[[UUIDIdentifier]]のインスタンス
   */
  def from(value: String): UUIDIdentifier = apply(UUID.nameUUIDFromBytes(value.getBytes))

  /** [[UUID]]を自動生成し、新しい[[Identifier]]のインスタンスを生成する。
   *
   *  @return 新しい[[UUIDIdentifier]]のインスタンス
   */
  def apply(): UUIDIdentifier = new UUIDIdentifier(UUID.randomUUID)

  /** [[UUIDIdentifier]]のための抽出子。
   *
   *  @param entityIdentifier [[UUIDIdentifier]]
   *  @return [[UUIDIdentifier]]のvalue
   */
  def unapply(entityIdentifier: UUIDIdentifier) = Some(entityIdentifier.value)
}

