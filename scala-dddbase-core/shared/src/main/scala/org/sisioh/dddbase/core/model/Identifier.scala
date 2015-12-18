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

import scala.scalajs.js.annotation.JSExport

/**
 * エンティティの識別子を表すトレイト。
 *
 * エンティティで用いられる識別子を型として表現することを目的としている。
 *
 * @tparam A 識別子の値を表す型
 */
trait Identifier[+A] extends Serializable {

  /**
   * 識別子の値を取得する。
   *
   * @return 識別子の値
   */
  def value: A

}

/**
 * 順序をサポートした`Identifier`。
 *
 * @tparam A 識別子の値を表す型
 * @tparam ID 識別子の型
 */
trait OrderedIdentifier[A, ID <: Identifier[A]]
  extends Identifier[A] with Ordered[ID]

/**
 * `OrderedIdentifier`の骨格実装。
 *
 * @tparam A 識別子の値を表す型
 * @tparam ID 識別子の型
 */
abstract class AbstractOrderedIdentifier[A <% Ordered[A], ID <: Identifier[A]]
    extends OrderedIdentifier[A, ID] {

  def compare(that: ID): Int = {
    value compare that.value
  }

}

/**
 * 識別子の値が空だった場合の例外。
 */
@JSExport
case class EmptyIdentifierException() extends Exception

/**
 * 空の識別子を表す値オブジェクト。
 */
@JSExport
class EmptyIdentifier
    extends Identifier[Nothing] {

  def value = throw EmptyIdentifierException()

  override def equals(obj: Any): Boolean = obj match {
    case that: EmptyIdentifier => this eq that
    case _                     => false
  }

  override def hashCode(): Int = 31 * 1

  override def toString = "EmptyIdentifier"
}

@JSExport
object EmptyIdentifier extends EmptyIdentifier

private[core] class IdentifierImpl[A](val value: A)
    extends Identifier[A] {

  override def equals(obj: Any) = obj match {
    case that: EmptyIdentifier => false
    case that: Identifier[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode = 31 * value.##

  override def toString = s"Identifier($value)"

}

/**
 * コンパニオンオブジェクト。
 */
@JSExport
object Identifier {

  /**
   * `Identifier`を生成する。
   *
   * @param value 識別子の値
   * @tparam A 識別子の値の型
   * @return `Identifier`
   */
  def apply[A](value: A): Identifier[A] = new IdentifierImpl(value)

  /**
   * 空の`Identifier`を返す。
   *
   * @return `Identifier`
   */
  def empty[A]: Identifier[A] = EmptyIdentifier

  /**
   * 抽出子メソッド。
   *
   * @param v `Identifier`
   * @tparam A 識別子の値の型
   * @return 識別子の値
   */
  def unapply[A](v: Identifier[A]): Option[A] = Some(v.value)

}

