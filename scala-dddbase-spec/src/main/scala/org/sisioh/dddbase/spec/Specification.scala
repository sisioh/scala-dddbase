package org.sisioh.dddbase.spec

/** 仕様を表すモデル。
 *
 *  <p>DDD本の中で説明している Specification パターンに則ったオブジェクトを表すインターフェイス。
 *  {@link Specification}の実装は、 {@link AbstractSpecification}を基底クラスとして実装するとよい。
 *  その場合、 {@link #isSatisfiedBy(Object)} を実装する必要しかない。</p>
 *
 *  @param <T> {@link Specification}の型
 */
trait Specification[T] {

  /** Create a new specification that is the AND operation of {@code this} specification and another specification.
   *
   *  @param specification Specification to AND.
   *  @return A new specification.
   *  @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def and(specification: Specification[T]): Specification[T] = new AndSpecification[T](this, specification)

  /** Check if {@code t} is satisfied by the specification.
   *
   *  @param t Object to test.
   *  @return {@code true} if {@code t} satisfies the specification.
   *  @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def isSatisfiedBy(t: T): Boolean

  /** Create a new specification that is the NOT operation of {@code this} specification.
   *
   *  @return A new specification.
   */
  def not: Specification[T] = new NotSpecification[T](this)

  /** Create a new specification that is the OR operation of {@code this} specification and another specification.
   *
   *  @param specification Specification to OR.
   *  @return A new specification.
   *  @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def or(specification: Specification[T]): Specification[T] = new OrSpecification(this, specification)

}
