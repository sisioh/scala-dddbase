package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

/**
 * Created with IntelliJ IDEA.
 * User: junichi
 * Date: 2013/07/14
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
trait TestAsyncRepository
  extends AsyncRepository[TestAsyncRepository, Identity[UUID], TestEntity] {

}
