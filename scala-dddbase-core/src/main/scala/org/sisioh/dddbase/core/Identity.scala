package org.sisioh.dddbase.core

trait Identity[A] {
  def value: A
}

private case class IdentityImpl[A](value: A) extends Identity[A]

object Identity {

  def apply[A](value: => A): Identity[A] = IdentityImpl(value)

  def unapply[A](v: Identity[A]): Option[A] = Some(v.value)

}


