package sttp.resilience4s.retry.syntax

import sttp.resilience4s.retry.RetryOps

trait RetrySyntax {
  implicit def retrySyntax[F[_], A](action: => F[A]): RetryOps[F, A] = new RetryOps[F, A](action)
}
