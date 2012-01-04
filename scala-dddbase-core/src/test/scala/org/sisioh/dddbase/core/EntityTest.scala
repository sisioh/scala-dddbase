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

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.scalatest.mock.MockitoSugar
import java.util.UUID
import scalaz._
import Scalaz._

/**
 * [[org.sisioh.dddbase.core.Entity]]のためのテスト。
 *
 * @author j5ik2o
 */
class EntityTest extends /*AssertionsForJUnit with*/ MockitoSugar {

  class TestEntity3(val identity: Identity[Int]) extends Entity[Int]

  class TestEntity(val identity: Identity[UUID]) extends Entity[UUID]
  class TestEntity2(val identity: Identity[UUID]) extends Entity[UUID]

  val id = Identity(UUID.randomUUID())

  import Implicits._

//  implicit val equalEntity: Equal[TestEntity] = equalA
//  implicit val showEntity: Show[TestEntity] = showA[TestEntity]


  @Test
  def test_コンストラクタで指定した識別子が取得できること {
    val entity:Entity[UUID] = new TestEntity(id)
    val entity2 = new TestEntity(id)

    assert(entity == entity2)
    assert(entity == entity)

    entity.shows
    entity.println
    assert(entity === entity2)
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

  val id = Identity(UUID.randomUUID())

  @Test
  def test_cloneしたインスタンスが等価であること {
    val entity1 = new Entity[UUID] with EntityCloneable[Entity[UUID], UUID] {
      val identity = id
    }
    val other = entity1.clone;
    assert(entity1 == other)
  }

}