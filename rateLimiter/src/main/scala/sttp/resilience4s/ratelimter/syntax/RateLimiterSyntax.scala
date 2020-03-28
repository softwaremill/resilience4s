package sttp.resilience4s.ratelimter.syntax

import sttp.resilience4s.ratelimter.RateLimiterOps

trait RateLimiterSyntax {
  implicit def rateLimiterSyntax[F[_], A](action: => F[A]): RateLimiterOps[F, A] = new RateLimiterOps[F, A](action)
}
