package org.sisioh.dddbase.spec

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
/**
 * [[AbstractSpecification]]のテストクラス。
 */
class SpecificationTest extends AssertionsForJUnit {
  /**
   * [[AbstractSpecification#and(Specification)]]
   * [[AbstractSpecification#or(Specification)]]
   * [[AbstractSpecification#not]]のテスト。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test01_and_or_not {
    val spec = new Specification[Unit] {
      def isSatisfiedBy(t: Unit): Boolean = false
    }
    assert(spec.and(spec).isInstanceOf[AndSpecification[Unit]])
    assert(spec.or(spec).isInstanceOf[OrSpecification[Unit]])
    assert(spec.not.isInstanceOf[NotSpecification[Unit]])
  }
}