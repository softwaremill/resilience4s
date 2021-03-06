package sttp.resilience4s.circuitbreaker

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import sttp.resilience4s.monad.MonadError

final class CircuitBreakerOps[F[_], A](action: => F[A]) {
  def withCircuitBreaker(
      circuitBreaker: CircuitBreaker
  )(implicit me: MonadError[F]): F[A] = {
    CircuitBreaker4s.decorateF(circuitBreaker, action)
  }
}
