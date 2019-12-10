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
package org.sisioh.dddbase.spec

import scala.scalajs.js.annotation.JSExportTopLevel

/**
  * ANDを表す仕様。
  *
  * @tparam T モデルの型
  */
@JSExportTopLevel("AndSpecification")
class AndSpecification[T](private[spec] val spec1: Specification[T],
                          private[spec] val spec2: Specification[T])
    extends Specification[T] {

  override def isSatisfiedBy(t: T) =
    spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t)
}
