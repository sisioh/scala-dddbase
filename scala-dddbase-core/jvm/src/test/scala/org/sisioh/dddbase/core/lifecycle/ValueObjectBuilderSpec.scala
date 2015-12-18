package org.sisioh.dddbase.core.lifecycle

import org.specs2.mutable._

class ValueObjectBuilderSpec extends Specification {

  case class PersonName(firstName: String, lastName: String)

  class PersonNameBuilder extends ValueObjectBuilder[PersonName, PersonNameBuilder] {
    private var firstName: String = _
    private var lastName: String = _

    def withFirstName(firstName: String) = {
      addConfigurator(_.firstName = firstName)
      getThis
    }

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

  object PersonNameBuilder {

    def apply() = new PersonNameBuilder()
  }

  "PersonNameBuilder" should {
    "build PersonName" in {
      val personName1 = PersonNameBuilder().withFirstName("Junichi").withLastName("Kato").build
      val PersonName(firstName, lastName) = personName1 // 抽出子を使ってそれぞれのプロパティに分解
      firstName must_== "Junichi"
      lastName must_== "Kato"
      val personName2 = PersonNameBuilder().withLastName(lastName.toUpperCase).build(personName1)
      personName2.firstName must_== "Junichi"
      personName2.lastName must_== "KATO"
    }
  }

}
