package org.sisioh.dddbase.spec

import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.mockito.Mockito._
import org.mockito.Matchers._
/**
 * Created by IntelliJ IDEA.
 * User: junichi
 * Date: 11/04/18
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */

class OrSpecificationTest extends AssertionsForJUnit with MockitoSugar {
  /**
   * {@code false} OR {@code false} が {@code false} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test01_falsefalse_To_false {
    val mock1 = mock[Specification[Unit]]
    val mock2 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(false)
    when(mock2.isSatisfiedBy(any(classOf[Unit]))).thenReturn(false)

    val or = new OrSpecification[Unit](mock1, mock2)
    assert(or.isSatisfiedBy(null) == false)

    verify(mock1).isSatisfiedBy(null)
    verify(mock2).isSatisfiedBy(null)
  }

  /**
   * {@code false} OR {@code true} が {@code true} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test02_falsetrue_To_true {
    val mock1 = mock[Specification[Unit]]
    val mock2 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(false)
    when(mock2.isSatisfiedBy(any(classOf[Unit]))).thenReturn(true)

    val or = new OrSpecification[Unit](mock1, mock2)
    assert(or.isSatisfiedBy(null) == true)

    verify(mock1).isSatisfiedBy(null);
    verify(mock2).isSatisfiedBy(null);
  }

  /**
   * {@code true} OR {@code false} が {@code true} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test03_truefalse_To_true {
    val mock1 = mock[Specification[Unit]]
    val mock2 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(true)
    when(mock2.isSatisfiedBy(any(classOf[Unit]))).thenReturn(false)

    val or = new OrSpecification[Unit](mock1, mock2)
    assert(or.isSatisfiedBy(null) == true)

    verify(mock1).isSatisfiedBy(null)
    //		verify(mock2, never()).isSatisfiedBy(null);
  }

  /**
   * {@code true} OR {@code true} が {@code true} となること。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test04_truetrue_To_true {
    val mock1 = mock[Specification[Unit]]
    val mock2 = mock[Specification[Unit]]

    when(mock1.isSatisfiedBy(any(classOf[Unit]))).thenReturn(true)
    when(mock2.isSatisfiedBy(any(classOf[Unit]))).thenReturn(true)

    val or = new OrSpecification[Unit](mock1, mock2)
    assert(or.isSatisfiedBy(null) == true)

    verify(mock1).isSatisfiedBy(null)
    //		verify(mock2, never()).isSatisfiedBy(null);
  }
}