package org.sisioh.dddbase.lifecycle.memory

//FIXME cross-build用オブジェクトの置き場が分からない
object JDKCollectionConvertersCompat {
  object Scope1 {
    object jdk {
      type CollectionConverters = Int
    }
  }
  import Scope1._

  object Scope2 {
    import scala.collection.{JavaConverters => CollectionConverters}
    object Inner {
      import scala._
      import jdk.CollectionConverters
      val Converters = CollectionConverters
    }
  }

  val Converters = Scope2.Inner.Converters
}
