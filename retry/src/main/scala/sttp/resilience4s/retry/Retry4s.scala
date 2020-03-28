package sttp.resilience4s.retry

import io.github.resilience4j.retry.Retry
import sttp.resilience4s.monad.MonadError
import sttp.resilience4s.monad.syntax._

object Retry4s {

  def decorateF[F[_], A](retry: Retry, action: => F[A])(implicit me: MonadError[F]): F[A] = {
    me.eval(retry.context[A]())
      .flatMap(ctx => loop[F, A](ctx, action))
  }

  private def loop[F[_], A](context: Retry.Context[A], action: => F[A])(implicit me: MonadError[F]): F[A] = {
    action
      .flatMap { value =>
        if (!context.onResult(value)) {
          me.eval(context.onComplete()).map(_ => value)
        } else {
          loop[F, A](context, action)
        }
      }
      .handleError {
        case ex: RuntimeException =>
          me.eval(context.onRuntimeError(ex)) >> loop[F, A](context, action)
      }
  }
}
