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
import scalaz.Identity
import java.util.UUID


class AggregateTest extends AssertionsForJUnit {

  case class Department(name: String)

  class Employee
  (val identity: Identity[UUID],
   var name: String,
   var dept: Department)
    extends Entity[UUID] with EntityCloneable[Employee,UUID] with AggregateRoot{
    override val aggregateIdentity = identity
    override def toString = "Employee(%s, %s, %s, %s)".format(identity, this.aggregateIdentity, name, dept)
  }

  object Employee {

    def apply(identifier: Identity[UUID], name: String, dept: Department) =
      new Employee(identifier, name, dept)

    def apply(name: String, dept: Department): Employee = apply(Identity(UUID.randomUUID), name, dept)

    def unapply(employee: Employee) = Some(employee.identity, employee.name, employee.dept)
  }

  @Test
  def test01_cloneしても不変条件を維持していること {
    val emp1 = Employee("KATO", Department("DEV"))
    val emp2 = emp1.clone
    expect(emp1)(emp2)
    assert(emp1 ne emp2)
    assert(emp1.identity eq emp2.identity)
    assert(emp1.name eq emp2.name)
    assert(emp1.dept eq emp2.dept)
    println(emp1)
    println(emp2)
  }

  object Position extends Enumeration {
    val FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT = Value
  }

  class Tire(val identity: Identity[UUID]) extends Entity[UUID] with EntityCloneable[Tire,UUID]
  object Tire {
    def apply(identifier: Identity[UUID]) = new Tire(identifier)
    def apply(): Tire = apply(Identity(UUID.randomUUID()))
  }

  class Car
  (val identity: Identity[UUID],
   var tires: Map[Position.Value, Tire])
    extends Entity[UUID] with EntityCloneable[Car,UUID]{

    override def clone: Car = {
      val result = super.clone.asInstanceOf[Car]
      result.tires = result.tires.map(e => (e._1, e._2.clone)).toMap
      result
    }

  }

  object Car {
    def apply(identifier: Identity[UUID], tires: Map[Position.Value, Tire]) =
      new Car(identifier, tires)

    def apply(tires: Map[Position.Value, Tire]): Car = apply(Identity(UUID.randomUUID()), tires)
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
    assert(car1.identity eq car2.identity)
    assert(car1.tires ne car2.tires)
    assert(car1.tires(Position.FRONT_LEFT) ne car2.tires(Position.FRONT_LEFT))
  }

}