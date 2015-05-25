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
package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.EntityReader
import org.sisioh.dddbase.core.model.{ Entity, Identifier }
import scala.util.{ Failure, Success, Try }

/**
 * 同期的に読み込むための`EntityReader`
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncEntityReader[ID <: Identifier[_], E <: Entity[ID]]
    extends EntityReader[ID, E, Try] {

  protected def mapValues[A, R](values: Try[A])(f: (A) => R)(implicit ctx: Ctx): Try[R] = {
    values.map(f)
  }

  protected def traverse[A, R](values: Seq[A], forceSuccess: Boolean)(f: (A) => Try[R])(implicit ctx: Ctx): Try[Seq[R]] = {
    values.map(f).foldLeft(Try(Seq.empty[R])) {
      (resultsTry, resultTry) =>
        (for { entities <- resultsTry; entity <- resultTry } yield entities :+ entity).recoverWith {
          case e => if (forceSuccess) Success(resultsTry.getOrElse(Seq.empty[R])) else Failure(e)
        }
    }
  }

}
