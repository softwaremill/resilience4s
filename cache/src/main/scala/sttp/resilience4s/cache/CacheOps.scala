package sttp.resilience4s.cache

import io.github.resilience4j.cache.Cache
import sttp.resilience4s.monad.MonadError

final class CacheOps[F[_], V](action: => F[V]) {
  def withCache[K](cache: Cache[K, V], key: K)(implicit me: MonadError[F]): F[V] =
    Cache4s.decorateF[K, V, F](cache, action, key)
}
