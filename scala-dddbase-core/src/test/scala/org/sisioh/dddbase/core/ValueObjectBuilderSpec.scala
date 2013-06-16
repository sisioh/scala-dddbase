package org.sisioh.dddbase.core

import org.specs2.mutable._

class ValueObjectBuilderSpec extends Specification {

  case class PersonName(firstName: String, lastName: String)

  /**
   * [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonName]]のための[[org.sisioh.dddbase.core.ValueObjectBuilder]]の実装。
   */
  class PersonNameBuilder extends ValueObjectBuilder[PersonName, PersonNameBuilder] {
    private var firstName: String = _
    private var lastName: String = _

    /**
     * [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonName]]に与える名前をビルダに設定する。
     *
     * @param firstName 名前
     * @return [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonNameBuilder]]
     */
    def withFirstName(firstName: String) = {
      addConfigurator(_.firstName = firstName)
      getThis
    }

    /**
     * [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonName]]に与える苗字をビルダに設定する。
     *
     * @param lastName 苗字
     * @return [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonNameBuilder]]
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
   * [[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonNameBuilder]]のためのコンパニオンオブジェクト。
   */
  object PersonNameBuilder {

    /**
     * 新しい[[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonNameBuilder]]を生成する。
     * @return 新しい[[org.sisioh.dddbase.core.ValueObjectBuilderSpec.PersonNameBuilder]]
     */
    def apply() = new PersonNameBuilder()
  }

  "PersonNameBuilder" should {
    "build PersonName" in {
      val personName1 = PersonNameBuilder().withFirstName("Junichi").withLastName("Kato").build
      val PersonName(firstName, lastName) = personName1 // 抽出子を使ってそれぞれのプロパティに分解
      assert(firstName == "Junichi")
      assert(lastName == "Kato")
      val personName2 = PersonNameBuilder().withLastName(lastName.toUpperCase).build(personName1)
      assert(personName2.firstName == "Junichi")
      assert(personName2.lastName == "KATO")
    }
  }


}
