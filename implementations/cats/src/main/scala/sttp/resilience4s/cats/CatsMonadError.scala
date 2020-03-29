package sttp.resilience4s.cats

import cats.effect.{Concurrent, Timer}
import sttp.resilience4s.monad.MonadError
import cats.effect.syntax.concurrent._

import scala.concurrent.duration.FiniteDuration

class CatsMonadError[F[_]](implicit me: cats.MonadError[F, Throwable], c: Concurrent[F], t: Timer[F])
    extends MonadError[F] {
  override def unit[T](t: T): F[T] = me.pure(t)

  override def map[T, T2](fa: F[T])(f: T => T2): F[T2] = me.map(fa)(f)

  override def flatMap[T, T2](fa: F[T])(f: T => F[T2]): F[T2] =
    me.flatMap(fa)(f)

  override def raiseError[T](t: Throwable): F[T] = me.raiseError(t)

  override protected def handleWrappedError[T](rt: F[T])(h: PartialFunction[Throwable, F[T]]): F[T] =
    me.recoverWith(rt)(h)

  override def timeout[T](fa: F[T], after: FiniteDuration): F[T] = fa.timeout(after)
}
