package org.sisioh.dddbase.core.lifecycle

/**
 * リポジトリの新しい状態とエンティティを保持する値オブジェクト。
 *
 * @param repository リポジトリの新しい状態
 * @param entity エンティティ
 * @tparam R リポジトリの型
 * @tparam T エンティティの型
 */
case class RepositoryWithEntity[+R, T](repository: R, entity: T)
