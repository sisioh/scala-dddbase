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
import org.scalatest.junit.AssertionsForJUnit

/**
 * `org.sisioh.dddbase.spec.Specification`のテストクラス。
 */
class SpecificationTest extends AssertionsForJUnit {
  /**
   * `org.sisioh.dddbase.spec.Specification# a n d ( S p e c i f i c a t i o n )`
   * `org.sisioh.dddbase.spec.Specification# o r ( S p e c i f i c a t i o n )`
   * `org.sisioh.dddbase.spec.Specification# n o t`のテスト。
   *
   * @throws Exception 例外が発生した場合
   */
  @Test
  def test01_and_or_not() {
    val spec = new Specification[Unit] {
      def isSatisfiedBy(t: Unit): Boolean = false
    }
    assert(spec.and(spec).isInstanceOf[AndSpecification[_]])
    assert(spec.or(spec).isInstanceOf[OrSpecification[_]])
    assert(spec.not.isInstanceOf[NotSpecification[_]])
  }
}