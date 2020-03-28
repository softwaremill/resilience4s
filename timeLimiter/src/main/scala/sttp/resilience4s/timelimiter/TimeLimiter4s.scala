package sttp.resilience4s.timelimiter

import java.util.concurrent.TimeUnit

import cats.effect.{Concurrent, Timer}
import io.github.resilience4j.timelimiter.TimeLimiter
import sttp.resilience4s.monad.MonadError

import scala.concurrent.duration.FiniteDuration
import cats.effect.implicits._
import sttp.resilience4s.monad.syntax._

import scala.concurrent.{ExecutionException, TimeoutException}

object TimeLimiter4s {

  def decorateF[F[_], A](
      timeLimiter: TimeLimiter,
      action: => F[A]
  )(implicit me: MonadError[F], timer: Timer[F], concurrent: Concurrent[F]): F[A] = {
    action
      .timeout(FiniteDuration(timeLimiter.getTimeLimiterConfig.getTimeoutDuration.toMillis, TimeUnit.MILLISECONDS))
      .flatMap(value => me.eval(timeLimiter.onSuccess()).map(_ => value)) //TODO handle shouldCancelRunningFuture
      .handleError {
        case ex: TimeoutException =>
          me.eval(timeLimiter.onError(ex)) >> me.raiseError(ex)
        case ex: ExecutionException =>
          val throwable = ex.getCause
          if (throwable == null) {
            me.eval(timeLimiter.onError(ex)) >> me.raiseError(ex)
          } else {
            me.eval(timeLimiter.onError(throwable)) >> me.raiseError(throwable)
          }
      }
  }
}
