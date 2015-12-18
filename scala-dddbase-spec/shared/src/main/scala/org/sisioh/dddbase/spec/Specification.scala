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
package org.sisioh.dddbase.spec

/**
 * 仕様を表すモデル。
 *
 * <p>DDD本の中で説明している Specification パターンに則ったオブジェクトを表すインターフェイス。
 * Specificationの実装は、 AbstractSpecification を基底クラスとして実装するとよい。
 * その場合、 `#isSatisfiedBy(Object)` を実装する必要しかない。</p>
 *
 * @tparam T { @link Specification}の型
 * @author j5ik2o
 */
trait Specification[T] {

  /**
   * Check if {@code t} is satisfied by the specification.
   *
   * @param t Object to test.
   * @return { @code true} if { @code t} satisfies the specification.
   * @throws `IllegalArgumentException` 引数に{ @code null}を与えた場合
   */
  def isSatisfiedBy(t: T): Boolean

  /**
   * Create a new specification that is the NOT operation of {@code this} specification.
   *
   * @return A new specification.
   */
  def not: Specification[T] = new NotSpecification[T](this)

  /**
   * Create a new specification that is the AND operation of {@code this} specification and another specification.
   *
   * @param specification Specification to AND.
   * @return A new specification.
   * @throws `IllegalArgumentException` 引数に{ @code null}を与えた場合
   */
  def and(specification: Specification[T]): Specification[T] = new AndSpecification(this, specification)

  /**
   * Create a new specification that is the OR operation of {@code this} specification and another specification.
   *
   * @param specification Specification to OR.
   * @return A new specification.
   * @throws `IllegalArgumentException` 引数に{ @code null}を与えた場合
   */
  def or(specification: Specification[T]): Specification[T] = new OrSpecification(this, specification)

}
