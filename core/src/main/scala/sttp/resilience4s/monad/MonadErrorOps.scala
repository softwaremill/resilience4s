package sttp.resilience4s.monad

final class MonadErrorOps[F[_], A](val r: F[A]) extends AnyVal {
  def map[B](f: A => B)(implicit ME: MonadError[F]): F[B] = ME.map(r)(f)
  def flatMap[B](f: A => F[B])(implicit ME: MonadError[F]): F[B] =
    ME.flatMap(r)(f)
  def >>[B](r2: F[B])(implicit ME: MonadError[F]): F[B] =
    ME.flatMap(r)(_ => r2)
  def handleError[T](h: PartialFunction[Throwable, F[A]])(
      implicit ME: MonadError[F]
  ): F[A] = ME.handleError(r)(h)
}
