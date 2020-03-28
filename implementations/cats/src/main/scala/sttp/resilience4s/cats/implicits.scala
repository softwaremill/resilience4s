package sttp.resilience4s.cats

import sttp.resilience4s.monad.MonadError

object implicits {
  implicit def catsMonadError[F[_]](implicit cme: cats.MonadError[F, Throwable]): MonadError[F] =
    new CatsMonadError[F]()
}
