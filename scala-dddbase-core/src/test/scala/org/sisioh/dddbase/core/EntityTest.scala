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

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.scalatest.mock.MockitoSugar

/**
 * [[Entity]]のためのテスト。
 *
 * @author j5ik2o
 */
class EntityTest extends AssertionsForJUnit with MockitoSugar {

  private val id = UUIDIdentifier(classOf[EntityTest])

  class TestEntity(val identifier: Identifier) extends Entity

  @Test
  def test_コンストラクタで指定した識別子が取得できること {
    val entity = new TestEntity(id)

    assert(entity.identifier == id)
  }

  @Test
  def test_識別子が同じならイコール演算子がtrueを返すこと {
    val entity1 = new TestEntity(id)

    val entity2 = new TestEntity(id)

    assert(entity1 == entity2)
  }

  @Test
  def test_識別子が同じならhashCodeが同じであること {
    val entity1 = new TestEntity(id)

    val entity2 = new TestEntity(id)

    assert(entity1.hashCode == entity2.hashCode)
  }

}

class EntityCloneableTest extends AssertionsForJUnit {

  private val id = UUIDIdentifier(classOf[EntityCloneableTest])

  @Test
  def test_cloneしたインスタンスが等価であること {
    val entity1 = new Entity with EntityCloneable[Entity] {
      val identifier = id
    }
    val other = entity1.clone;
    assert(entity1 == other)
  }

}