package sttp.resilience4s.monad

final class MonadErrorValueOps[F[_], A](val v: A) extends AnyVal {
  def unit(implicit ME: MonadError[F]): F[A] = ME.unit(v)
}
