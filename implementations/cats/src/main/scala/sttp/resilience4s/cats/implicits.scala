package sttp.resilience4s.cats

import cats.effect.{Concurrent, Timer}
import sttp.resilience4s.monad.MonadError

object implicits {
  implicit def catsMonadError[F[_]](
      implicit cme: cats.MonadError[F, Throwable],
      c: Concurrent[F],
      t: Timer[F]
  ): MonadError[F] =
    new CatsMonadError[F]()
}
