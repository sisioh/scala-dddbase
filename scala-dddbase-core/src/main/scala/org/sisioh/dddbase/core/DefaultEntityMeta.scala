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

import scalaz.Identity

/**
 * [[org.sisioh.dddbase.core.EntityMeta]]のデフォルト実装。
 *
 * @tparam T 参照対象オブジェクトの型
 * @param identifier [[scalaz.Identity]]
 * @author j5ik2o
 */
case class DefaultEntityMeta[T <: Entity[ID], ID <: java.io.Serializable](identifier: Identity[ID]) extends EntityMeta[T, ID] {

  def this(referent: T) = this(referent.identifier)

  def isReferenceOf(target: Entity[ID]) =
    identifier == target.identifier

}