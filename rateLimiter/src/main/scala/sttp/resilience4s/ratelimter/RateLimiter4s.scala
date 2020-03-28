package sttp.resilience4s.ratelimter

import io.github.resilience4j.ratelimiter.RateLimiter
import sttp.resilience4s.monad.MonadError
import sttp.resilience4s.monad.syntax._

object RateLimiter4s {

  def decorateF[F[_], T](
      rateLimiter: RateLimiter,
      action: => F[T]
  )(implicit monadError: MonadError[F]): F[T] = {
    monadError.eval(RateLimiter.waitForPermission(rateLimiter)) >> action
  }
}
