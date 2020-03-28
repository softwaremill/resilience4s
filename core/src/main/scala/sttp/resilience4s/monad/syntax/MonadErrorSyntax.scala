package sttp.resilience4s.monad.syntax

import sttp.resilience4s.monad.{MonadErrorOps, MonadErrorValueOps}

trait MonadErrorSyntax {

  implicit def monadErrorOps[F[_], A](action: => F[A]): MonadErrorOps[F, A] =
    new MonadErrorOps[F, A](action)

  implicit def monadErrorValueOps[F[_], A](value: A): MonadErrorValueOps[F, A] =
    new MonadErrorValueOps[F, A](value)
}
