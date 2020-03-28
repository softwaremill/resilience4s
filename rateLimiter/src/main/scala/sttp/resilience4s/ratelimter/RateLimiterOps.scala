package sttp.resilience4s.ratelimter

import io.github.resilience4j.ratelimiter.RateLimiter
import sttp.resilience4s.RateLimiter4s
import sttp.resilience4s.monad.MonadError

final class RateLimiterOps[F[_], A](action: F[A]) {
  def withRateLimiter(rateLimiter: RateLimiter)(implicit me: MonadError[F]): F[A] = {
    RateLimiter4s.decorateF(rateLimiter, action)
  }
}
