package org.sisioh.dddbase.core

@cloneable
trait EntityCloneable[T <: Entity] {
  this: Entity =>

  /** クローンを生成する。
   *
   *  @return クローンしたインスタンス
   */
  override def clone: T =
    super.clone.asInstanceOf[T]
}
