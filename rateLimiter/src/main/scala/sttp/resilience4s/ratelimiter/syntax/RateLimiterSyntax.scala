package sttp.resilience4s.ratelimiter.syntax

import sttp.resilience4s.ratelimiter.RateLimiterOps

trait RateLimiterSyntax {
  implicit def rateLimiterSyntax[F[_], A](action: => F[A]): RateLimiterOps[F, A] = new RateLimiterOps[F, A](action)
}
