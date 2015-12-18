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
package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.lifecycle.EntityReader
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.concurrent.Future

/**
 * 非同期版`org.sisioh.dddbase.core.lifecycle.EntityReader`。
 *
 * @see `org.sisioh.dddbase.core.lifecycle.EntityReader`
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReader[ID <: Identifier[_], E <: Entity[ID]]
    extends AsyncEntityIO with EntityReader[ID, E, Future] {

  protected def mapValues[A, R](values: Future[A])(f: (A) => R)(implicit ctx: Ctx): Future[R] = {
    implicit val executor = getExecutionContext(ctx)
    values.map(f)
  }

  protected def traverse[A, R](values: Seq[A], forceSuccess: Boolean)(f: (A) => Future[R])(implicit ctx: Ctx): Future[Seq[R]] = {
    implicit val executor = getExecutionContext(ctx)
    values.map(f).foldLeft(Future.successful(Seq.empty[R])) {
      (resultsFuture, resultFuture) =>
        (for { results <- resultsFuture; result <- resultFuture } yield results :+ result).recoverWith {
          case e => if (forceSuccess) resultsFuture else Future.failed(e)
        }
    }
  }

}
