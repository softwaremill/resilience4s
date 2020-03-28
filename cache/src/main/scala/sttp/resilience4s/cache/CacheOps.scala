package sttp.resilience4s.cache

import io.github.resilience4j.cache.Cache
import sttp.resilience4s.monad.MonadError

final class CacheOps[F[_], A](action: => F[A]) {
  def withCache[K, V](cache: Cache[K, V], key: K)(implicit me: MonadError[F]): F[A] =
    Cache4s.decorateF(cache, action, key)
}
