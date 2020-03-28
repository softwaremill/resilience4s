package sttp.resilience4s.circutbreaker.syntax

import sttp.resilience4s.circutbreaker.CircuitBreakerOps

trait CircuitBreakerSyntax {
  implicit final def circuitBreakerSyntax[F[_], A](
      action: => F[A]
  ): CircuitBreakerOps[F, A] =
    new CircuitBreakerOps[F, A](action)
}
