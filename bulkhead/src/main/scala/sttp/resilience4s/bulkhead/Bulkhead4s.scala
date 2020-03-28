package sttp.resilience4s.bulkhead

import io.github.resilience4j.bulkhead.Bulkhead
import sttp.resilience4s.monad.MonadError
import sttp.resilience4s.monad.syntax._

object Bulkhead4s {

  def decorateF[F[_], A](bulkhead: Bulkhead, action: => F[A])(implicit me: MonadError[F]): F[A] = {
    (me.eval(bulkhead.acquirePermission()) >> action)
      .handleError {
        case e =>
          me.eval(bulkhead.onComplete()) >> me.raiseError(e)
      }
  }
}
