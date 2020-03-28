package sttp.resilience4s.retry

import io.github.resilience4j.retry.Retry
import sttp.resilience4s.monad.MonadError

final class RetryOps[F[_], A](action: => F[A]) {
  def withRetry(retry: Retry)(implicit me: MonadError[F]): F[A] = Retry4s.decorateF(retry, action)
}
