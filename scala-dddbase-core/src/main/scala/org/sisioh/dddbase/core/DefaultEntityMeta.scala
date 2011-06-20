package org.sisioh.dddbase.core

/**[[org.sisioh.dddbase.core.EntityMeta]]のデフォルト実装。
 *
 * @tparam T 参照対象オブジェクトの型
 * @param identifier [[org.sisioh.dddbase.core.Identifier]]
 * @author j5ik2o
 */
case class DefaultEntityMeta[T <: Entity]
(identifier: Identifier) extends EntityMeta[T] {

  def this(referent: T) = this (referent.identifier)

  def isReferenceOf(target: Entity) =
    identifier == target.identifier

}