package org.sisioh.dddbase.core

import collection.mutable.ListBuffer

/** 値オブジェクトのインスタンスを生成するビルダーのトレイト。
 *
 *  @tparam T ビルド対象のインスタンスの型
 *  @tparam S このビルダークラスの型
 */
trait ValueObjectBuilder[T, S <: ValueObjectBuilder[T, S]] {
  type Configure = (S) => Unit

  /** ビルダの設定に基づいて[[ValueObject]]の新しいインスタンスを生成する。
   *
   *  @return [[ValueObject]]の新しいインスタンス
   */
  def build: T = {
    configurators.foreach(_(getThis))
    createValueObject
  }

  /** このビルダークラスのインスタンスを返す。
   *
   *  @return このビルダークラスのインスタンス。
   */
  protected def getThis: S

  /** ビルダの設定に基づき、引数の[[ValueObject]]の内容を変更した新しいインスタンスを生成する。
   *
   *  @param vo 状態を引用する[[ValueObject]]
   *  @return voの内容に対して、このビルダの設定を上書きした[[ValueObject]]の新しいインスタンス
   */
  def build(vo: T): T = {
    val builder = newInstance
    apply(vo, builder)
    configurators.foreach(builder.addConfigurator(_))
    builder.build
  }

  /** ビルダを設定する関数を追加する。
   *
   *  @param configurator [[Configure]]
   */
  protected def addConfigurator(configure: Configure): Unit = {
    configurators += configure
  }

  /** このビルダークラスの新しいインスタンスを返す。
   *
   *  @return このビルダークラスの新しいインスタンス。
   */
  protected def newInstance: S

  /** 引数のビルダに対して、引数の[[ValueObject]]の内容を適用する。
   *
   *  @param vo 状態を引用する[[ValueObject]]
   *  @param builder ビルダ
   */
  protected def apply(vo: T, builder: S): Unit

  /** ビルダの設定に基づいて[[ValueObject]]の新しいインスタンスを生成する。
   *
   *  <p>[[ValueObjectBuilder]]のbuild内でこのビルダに追加された[[Configure]]を全て実行した後に、このメソッドが呼ばれる。<br>
   *  その為、このビルダに対する変更を行うロジックはこのメソッド内に記述せず、目的となる[[ValueObject]]を生成し
   *  返すロジックを記述することが望まれる。</p>
   *
   *  @return [[ValueObject]]の新しいインスタンス
   */
  protected def createValueObject: T

  private val configurators = new ListBuffer[Configure]
}