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
package org.sisioh.dddbase.core.lifecycle

import collection.mutable.ListBuffer

/**
 * 値オブジェクトのインスタンスを生成するビルダーのトレイト。
 *
 * @tparam T ビルド対象のインスタンスの型
 * @tparam S このビルダークラスの型
 * @author j5ik2o
 */
trait ValueObjectBuilder[T, S <: ValueObjectBuilder[T, S]] {
  type Configure = (S) => Unit

  /**
   * ビルダの設定に基づいて値オブジェクトの新しいインスタンスを生成する。
   *
   * @return 値オブジェクトの新しいインスタンス
   */
  def build: T = {
    configurators.foreach(_(getThis))
    createValueObject
  }

  /**
   * このビルダークラスのインスタンスを返す。
   *
   * @return このビルダークラスのインスタンス。
   */
  protected def getThis: S

  /**
   * ビルダの設定に基づき、引数の値オブジェクトの内容を変更した新しいインスタンスを生成する。
   *
   * @param vo 状態を引用する値オブジェクト
   * @return voの内容に対して、このビルダの設定を上書きした値オブジェクトの新しいインスタンス
   */
  def build(vo: T): T = {
    val builder = newInstance
    apply(vo, builder)
    configurators.foreach(builder.addConfigurator)
    builder.build
  }

  /**
   * ビルダを設定する関数を追加する。
   *
   * @param configure `Configure`
   */
  protected def addConfigurator(configure: Configure): Unit = {
    configurators += configure
  }

  /**
   * このビルダークラスの新しいインスタンスを返す。
   *
   * @return このビルダークラスの新しいインスタンス。
   */
  protected def newInstance: S

  /**
   * 引数のビルダに対して、引数の値オブジェクトの内容を適用する。
   *
   * @param vo 状態を引用する値オブジェクト
   * @param builder ビルダ
   */
  protected def apply(vo: T, builder: S): Unit

  /**
   * ビルダの設定に基づいて値オブジェクトの新しいインスタンスを生成する。
   *
   * <p>`build` 内でこのビルダに追加された `Configure` を全て実行した後に、このメソッドが呼ばれる。<br>
   * その為、このビルダに対する変更を行うロジックはこのメソッド内に記述せず、目的となる値オブジェクトを生成し
   * 返すロジックを記述することが望まれる。</p>
   *
   * @return 値オブジェクトの新しいインスタンス
   */
  protected def createValueObject: T

  private val configurators = new ListBuffer[Configure]

}