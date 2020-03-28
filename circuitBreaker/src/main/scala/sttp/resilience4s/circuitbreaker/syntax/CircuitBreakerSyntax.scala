package sttp.resilience4s.circuitbreaker.syntax

import sttp.resilience4s.circuitbreaker.CircuitBreakerOps

trait CircuitBreakerSyntax {
  implicit final def circuitBreakerSyntax[F[_], A](
      action: => F[A]
  ): CircuitBreakerOps[F, A] =
    new CircuitBreakerOps[F, A](action)
}
