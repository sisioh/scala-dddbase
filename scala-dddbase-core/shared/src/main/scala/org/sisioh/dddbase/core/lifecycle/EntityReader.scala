/*
 * Copyright 2011-2013 Sisioh Project and others. (http://www.sisioh.org/)
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
package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.language.reflectiveCalls

/**
 * `Identifier` を用いて `Entity` を読み込むための責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait EntityReader[ID <: Identifier[_], E <: Entity[ID], M[+_]]
    extends EntityIO[M] {
  self =>

  protected def traverseWithoutFailures[A, R](values: Seq[A])(f: (A) => M[R])(implicit ctx: Ctx): M[Seq[R]] =
    traverse[A, R](values, forceSuccess = true)(f)

  protected def traverse[A, R](values: Seq[A], forceSuccess: Boolean = false)(f: (A) => M[R])(implicit ctx: Ctx): M[Seq[R]]

  protected def mapValues[A, R](values: M[A])(f: (A) => R)(implicit ctx: Ctx): M[R]

  private implicit def MapExtension[A](values: M[A]) = new {

    def mapValues[R](f: (A) => R)(implicit ctx: Ctx): M[R] = self.mapValues(values)(f)

  }

  def resolveBy(identifier: ID)(implicit ctx: Ctx): M[E]

  def resolveByMulti(identifiers: ID*)(implicit ctx: Ctx): M[Seq[E]] =
    traverseWithoutFailures(identifiers)(resolveBy)

  def apply(identifier: ID)(implicit ctx: Ctx): M[E] = resolveBy(identifier)

  def existBy(identifier: ID)(implicit ctx: Ctx): M[Boolean]

  def existByMulti(identifiers: ID*)(implicit ctx: Ctx): M[Boolean] =
    traverseWithoutFailures(identifiers)(existBy).mapValues(_.forall(_ == true))

  def exist(entity: E)(implicit ctx: Ctx): M[Boolean] =
    existBy(entity.identifier)

  def existMulti(entities: E*)(implicit ctx: Ctx): M[Boolean] =
    existByMulti(entities.map(_.identifier): _*)

}

