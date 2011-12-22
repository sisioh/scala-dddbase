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

import java.util.UUID

/**
 * 識別子を表すトレイト。
 *
 * @author j5ik2o
 */
trait Identifier extends Serializable {
  type IdType

  val kind: String

  val value: IdType

  override def equals(that: Any): Boolean = that match {
    case other: Identifier => kind == other.kind && value == other.value
    case _ => false
  }

  override def hashCode = value.hashCode

  override def toString = "Identifier(%s,%s)".format(kind, value)

}

object Identifier {

  def assignName(klass: Class[_], name: String) = new Identifier {
    type IdType = String
    val kind = klass.getName()
    val value = name
  }

  def assignId(klass: Class[_], id: Long) = new Identifier {
    type IdType = Long
    val kind = klass.getName()
    val value = id
  }

  def alocateId(klass: Class[_])(implicit idGenerator: () => Long) = assignId(klass, idGenerator())

  def alocateUUID(klass: Class[_], uuid: UUID) = UUIDIdentifier(klass, uuid)

  def alocateUUID(klass: Class[_]) = UUIDIdentifier(klass)

}

/**
 * [[java.util.UUID]]を使った[[org.sisioh.dddbase.core.Identifier]]の実装クラス。
 *
 * @author j5ik2o
 * @param value [[java.util.UUID]]
 */
class UUIDIdentifier(val kind: String, val value: UUID) extends Identifier {
  type IdType = UUID
  override def toString = "UUIDIdentifier(%s)".format(value)
}

/**
 * `UUIDIdentifier`クラスのための、コンパニオンオブジェクト。
 *
 * @author j5ik2o
 */
object UUIDIdentifier {

  /**
   * [[java.util.UUID]]を基に、新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(kind: Class[_], value: UUID): UUIDIdentifier = new UUIDIdentifier(kind.getName, value)

  /**
   * 文字列形式のUUIDを新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(kind: Class[_], value: String): UUIDIdentifier = apply(kind, UUID.fromString(value))

  /**
   * 文字列を基に、新しいインスタンスを生成する。
   *
   * @param value [[java.util.UUID]]
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def from(kind: Class[_], value: String): UUIDIdentifier = apply(kind, UUID.nameUUIDFromBytes(value.getBytes))

  /**
   * [[java.util.UUID]]を自動生成し、新しいインスタンスを生成する。
   *
   * @return 新しい[[org.sisioh.dddbase.core.UUIDIdentifier]]のインスタンス
   */
  def apply(kind: Class[_]): UUIDIdentifier = apply(kind, UUID.randomUUID)

  /**
   * [[org.sisioh.dddbase.core.UUIDIdentifier]]のための抽出子メソッド。
   *
   * @param entityIdentifier [[org.sisioh.dddbase.core.UUIDIdentifier]]
   * @return [[org.sisioh.dddbase.core.UUIDIdentifier]]のvalue
   */
  def unapply(entityIdentifier: UUIDIdentifier): Option[(String, UUID)] = Some(entityIdentifier.kind, entityIdentifier.value)
}

