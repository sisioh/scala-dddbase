package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.Identity
import java.util.UUID

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
