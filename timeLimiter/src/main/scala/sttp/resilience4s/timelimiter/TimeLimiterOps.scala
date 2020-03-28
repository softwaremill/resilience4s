package sttp.resilience4s.timelimiter

import cats.effect.{Concurrent, Timer}
import io.github.resilience4j.timelimiter.TimeLimiter
import sttp.resilience4s.monad.MonadError

final class TimeLimiterOps[F[_], A](action: => F[A]) {
  def withTimeLimiter(
      timeLimiter: TimeLimiter
  )(implicit me: MonadError[F], timer: Timer[F], concurrent: Concurrent[F]): F[A] =
    TimeLimiter4s.decorateF(timeLimiter, action)
}
