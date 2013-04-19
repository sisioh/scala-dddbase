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

/**
 * エンティティを表すトレイト。
 *
 * @author j5ik2o
 */
trait Entity[ID <: Identity[_]] {

  /** エンティティの識別子。 */
  val identity: ID

  /**
   * ハッシュコードを返す。
   *
   * @return ハッシュコード
   */
  override final def hashCode: Int = identity.##

  /**
   * 指定されたオブジェクトと等価であるかを判定する。
   *
   * @param that オブジェクト
   * @return 等価である場合はtrue
   */
  override final def equals(that: Any): Boolean = that match {
    case that: Entity[_] => identity == that.identity
    case _ => false
  }

  // var version: Long = _

  //  var uncommittedEventCount:Int = _

  //  var uncommittedEvents = Stream.empty[DomainEvent[_]]

  //  def commitEvents

}


/**
 * クローンに対応したエンティティを実装するためのトレイト。
 *
 * @author j5ik2o
 */
trait EntityCloneable[ID <: Identity[_], T <: Entity[ID]] extends Cloneable {
  this: Entity[ID] =>

  /**
   * クローンを生成する。
   *
   * @return クローンしたインスタンス
   */
  override def clone: T =
    super.clone.asInstanceOf[T]

}

/**
 * シリアライズに対応したエンティティを実装するためのトレイト。
 *
 * @author mtgto
 */
trait EntitySerializable[ID <: Identity[_ <: java.io.Serializable], T <: Entity[ID]] extends Serializable {
  this: Entity[ID] =>
}