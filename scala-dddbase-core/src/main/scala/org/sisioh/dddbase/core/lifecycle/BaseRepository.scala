package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * 基本的なリポジトリのトレイト。
 * リポジトリとして、基本的に必要な機能を定義するトレイト。
 *
 * リポジトリの状態を変更するメソッドの戻り値としては、
 * Immutableなリポジトリは新しいリポジトリインスタンスを返し、
 * Mutableなリポジトリは同一インスタンスを返すこと、を推奨する。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 * @tparam M モナドの型
 */
trait BaseRepository[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends BaseEntityReader[ID, E, M] with BaseEntityWriter[ID, E, M] {

  type This <: BaseRepository[ID, E, M]

}
