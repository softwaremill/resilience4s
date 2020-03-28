package sttp.resilience4s.timelimiter.syntax

import sttp.resilience4s.timelimiter.TimeLimiterOps

trait TimeLimiterSyntax {
  implicit final def timeLimiterSyntax[F[_], A](action: => F[A]): TimeLimiterOps[F, A] =
    new TimeLimiterOps[F, A](action)
}
