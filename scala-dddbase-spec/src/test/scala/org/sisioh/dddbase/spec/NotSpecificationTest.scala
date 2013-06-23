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

import org.junit.Test
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar

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