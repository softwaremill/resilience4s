package sttp.resilience4s.cache.syntax

import sttp.resilience4s.cache.CacheOps

trait CacheSyntax {
  implicit def cacheSyntax[F[_], A](action: => F[A]): CacheOps[F, A] = new CacheOps[F, A](action)
}
