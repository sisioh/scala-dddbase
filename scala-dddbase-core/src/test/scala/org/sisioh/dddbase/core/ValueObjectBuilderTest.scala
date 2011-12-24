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

/**
 * [[ValueObjectBuilder]]のためのテスト。
 */
class ValueObjectBuilderTest extends AssertionsForJUnit {

  case class PersonName(firstName: String, lastName: String)

  /**
   * [[PersonName]]のための[[ValueObjectBuilder]]の実装。
   */
  class PersonNameBuilder extends ValueObjectBuilder[PersonName, PersonNameBuilder] {
    private var firstName: String = _
    private var lastName: String = _

    /**
     * [[PersonName]]に与える名前をビルダに設定する。
     *
     * @param firstName 名前
     * @return [[PersonNameBuilder]]
     */
    def withFirstName(firstName: String) = {
      addConfigurator(_.firstName = firstName)
      getThis
    }

    /**
     * [[PersonName]]に与える苗字をビルダに設定する。
     *
     * @param lastName 苗字
     * @return [[PersonNameBuilder]]
     */
    def withLastName(lastName: String) = {
      addConfigurator(_.lastName = lastName)
      getThis
    }

    protected def newInstance = PersonNameBuilder()

    protected def getThis = this

    protected def createValueObject = PersonName(firstName, lastName)

    protected def apply(vo: PersonName, builder: PersonNameBuilder) = {
      builder.withFirstName(vo.firstName)
      builder.withLastName(vo.lastName)
    }
  }

  /**
   * [[PersonNameBuilder]]のためのコンパニオンオブジェクト。
   */
  object PersonNameBuilder {

    /**
     * 新しい[[PersonNameBuilder]]を生成する。
     * @return 新しい[[PersonNameBuilder]]
     */
    def apply() = new PersonNameBuilder()
  }

  @Test
  def test01_ビルドできること {
    val personName1 = PersonNameBuilder().withFirstName("Junichi").withLastName("Kato").build
    val PersonName(firstName, lastName) = personName1 // 抽出子を使ってそれぞれのプロパティに分解
    assert(firstName == "Junichi")
    assert(lastName == "Kato")
    val personName2 = PersonNameBuilder().withLastName(lastName.toUpperCase).build(personName1)
    assert(personName2.firstName == "Junichi")
    assert(personName2.lastName == "KATO")
  }

}