package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.model._
import org.sisioh.dddbase.spec.Specification

import scala.language.higherKinds

/**
 * `Specification` を使った検索を行うためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 * @tparam M モナドの型
 */
trait EntityReadableBySpecification[ID <: Identifier[_], T <: Entity[ID], M[+A]] {

  /**
   * `Specification` に該当したエンティティを取得する。
   *
   * @param specification `Specification`
   * @return モナドにラップされた `EntitiesChunk`
   */
  def filterBySpecification(specification: Specification[T], index: Option[Int] = None, maxEntities: Option[Int] = None): M[EntitiesChunk[ID, T]]

}
