package sttp.resilience4s.bulkhead

import io.github.resilience4j.bulkhead.Bulkhead
import sttp.resilience4s.monad.MonadError

final class BulkheadOps[F[_], A](action: => F[A]) {
  def withBulkhead(bulkhead: Bulkhead)(implicit me: MonadError[F]): F[A] = Bulkhead4s.decorateF(bulkhead, action)
}
