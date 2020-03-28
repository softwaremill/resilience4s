package sttp.resilience4s.cache

import io.github.resilience4j.cache.Cache
import io.github.resilience4j.cache.internal.CacheImpl
import io.vavr.control
import sttp.resilience4s.monad.MonadError
import sttp.resilience4s.monad.syntax._

object Cache4s {
  def decorateF[K, V, F[_]](cache: Cache[K, V], action: => F[V], key: K)(implicit me: MonadError[F]): F[V] = {
    val cacheImpl = cache.asInstanceOf[CacheImpl[K, V]]
    new AccessibleCache(cacheImpl).computeIfAbsent(key, action)
  }
}

class AccessibleCache[K, V](cacheImpl: CacheImpl[K, V]) {

  private def putValueIntoCache(key: K, value: V): Unit = {
    val method = cacheImpl.getClass.getDeclaredMethod("putValueIntoCache")
    method.setAccessible(true)
    method.invoke(cacheImpl, key, value)
  }

  private def getValueFromCache(cacheKey: K): Option[V] = {
    val method = cacheImpl.getClass.getDeclaredMethod("getValueFromCache")
    method.setAccessible(true)
    val vavrOption = method.invoke(cacheImpl, cacheKey).asInstanceOf[control.Option[V]]
    if (vavrOption.isEmpty) {
      None
    } else {
      Some(vavrOption.get())
    }
  }

  def computeIfAbsent[F[_]](key: K, action: => F[V])(implicit me: MonadError[F]): F[V] = {
    me.eval(getValueFromCache(key)).flatMap {
      case Some(value) => me.unit(value)
      case None =>
        action.map { value =>
          putValueIntoCache(key, value)
          value
        }
    }
  }
}
