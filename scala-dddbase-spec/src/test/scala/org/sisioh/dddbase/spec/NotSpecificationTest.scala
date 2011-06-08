package org.sisioh.dddbase.spec

import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.AssertionsForJUnit
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.Test
/**
 * Created by IntelliJ IDEA.
 * User: junichi
 * Date: 11/04/18
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */

class NotSpecificationTest extends AssertionsForJUnit with MockitoSugar {
  /**
   * NOT {@code false} が {@code true} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test01_false_To_true {
    val mock1 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(false)

    val not = new NotSpecification[Unit](mock1)
    assert(not.isSatisfiedBy(null) == true)

    verify(mock1).isSatisfiedBy(null)
  }

  /**
   * NOT {@code true} が {@code false} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test01_true_To_false {
    val mock1 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(true)

    val not = new NotSpecification[Unit](mock1)
    assert(not.isSatisfiedBy(null) == false)

    verify(mock1).isSatisfiedBy(null)
  }
}