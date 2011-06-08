package org.sisioh.dddbase.core

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test


class AggregateTest extends AssertionsForJUnit {

  case class Department(name: String)

  class Employee
  (val identifier: Identifier,
   var name: String,
   var dept: Department)
    extends Entity with EntityCloneable[Employee]{
    override def toString = "Employee(%s, %s, %s)".format(identifier, name, dept)
  }

  object Employee {

    def apply(identifier: Identifier, name: String, dept: Department) =
      new Employee(identifier, name, dept)

    def apply(name: String, dept: Department): Employee = apply(UUIDIdentifier(), name, dept)

    def unapply(employee: Employee) = Some(employee.identifier, employee.name, employee.dept)
  }

  @Test
  def test01_cloneしても不変条件を維持していること {
    val emp1 = Employee("KATO", Department("DEV"))
    val emp2 = emp1.clone
    expect(emp1)(emp2)
    assert(emp1 ne emp2)
    assert(emp1.identifier eq emp2.identifier)
    assert(emp1.name eq emp2.name)
    assert(emp1.dept eq emp2.dept)
    println(emp1)
    println(emp2)
  }

  object Position extends Enumeration {
    val FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT = Value
  }

  class Tire(val identifier: Identifier) extends Entity with EntityCloneable[Tire]
  object Tire {
    def apply(identifier: Identifier) = new Tire(identifier)
    def apply(): Tire = apply(UUIDIdentifier())
  }

  class Car
  (val identifier: Identifier,
   var tires: Map[Position.Value, Tire])
    extends Entity with EntityCloneable[Car]{

    override def clone: Car = {
      val result = super.clone.asInstanceOf[Car]
      result.tires = result.tires.map(e => (e._1, e._2.clone)).toMap
      result
    }

  }

  object Car {
    def apply(identifier: Identifier, tires: Map[Position.Value, Tire]) =
      new Car(identifier, tires)

    def apply(tires: Map[Position.Value, Tire]): Car = apply(UUIDIdentifier(), tires)
  }

  @Test
  def test02_cloneしても不変条件を維持していること {
    val car1 = Car(
      Map(Position.FRONT_LEFT -> Tire(),
      Position.FRONT_RIGHT -> Tire(),
      Position.BACK_LEFT -> Tire(),
      Position.BACK_RIGHT -> Tire()))

    val car2 = car1.clone
    expect(car1)(car2)
    assert(car1 ne car2)
    assert(car1.identifier eq car2.identifier)
    assert(car1.tires ne car2.tires)
    assert(car1.tires(Position.FRONT_LEFT) ne car2.tires(Position.FRONT_LEFT))
  }

}