package org.sisioh.dddbase.core

/**
 * 基底例外クラス。
 *
 * @author j5ik2o
 */
abstract class BaseException(message: Option[String] = None, cause: Option[Throwable] = None)
  extends Exception(message.orNull, cause.orNull)