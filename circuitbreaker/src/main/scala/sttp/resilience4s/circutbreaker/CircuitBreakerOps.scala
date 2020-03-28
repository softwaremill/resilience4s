package sttp.resilience4s.circutbreaker

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import sttp.resilience4s.CircuitBreaker4s
import sttp.resilience4s.circutbreaker.monad.MonadError

class CircuitBreakerOps[F[_], A](action: => F[A]) {
  def withCircuitBreaker(
    circuitBreaker: CircuitBreaker
  )(implicit me: MonadError[F]): F[A] = {
    CircuitBreaker4s.decorateF(circuitBreaker, action)
  }
}
