package sttp.resilience4s

import java.util.concurrent.TimeUnit

import io.github.resilience4j.circuitbreaker.{CallNotPermittedException, CircuitBreaker}
import sttp.resilience4s.monad.syntax._
import sttp.resilience4s.monad.MonadError

package object CircuitBreaker4s {

  def decorateF[F[_], T](circuitBreaker: CircuitBreaker, action: => F[T])(
      implicit monadError: MonadError[F]
  ): F[T] = {
    monadError.flatMap(monadError.unit(())) { _ =>
      if (!circuitBreaker.tryAcquirePermission()) {
        monadError.raiseError(
          CallNotPermittedException
            .createCallNotPermittedException(circuitBreaker)
        )
      } else {
        val start = System.nanoTime()
        try {
          action
            .map { r =>
              circuitBreaker.onSuccess(
                System.nanoTime() - start,
                TimeUnit.NANOSECONDS
              )
              r
            }
            .handleError {
              case t =>
                circuitBreaker.onError(
                  System.nanoTime() - start,
                  TimeUnit.NANOSECONDS,
                  t
                )
                monadError.raiseError(t)
            }
        } catch {
          case t: Throwable =>
            circuitBreaker.onError(
              System.nanoTime() - start,
              TimeUnit.NANOSECONDS,
              t
            )
            monadError.raiseError(t)
        }
      }
    }
  }
}
