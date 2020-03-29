package sttp.resilience4s.monad

import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success, Try}

trait MonadError[F[_]] {
  def unit[T](t: T): F[T]
  def map[T, T2](fa: F[T])(f: T => T2): F[T2]
  def flatMap[T, T2](fa: F[T])(f: T => F[T2]): F[T2]

  def raiseError[T](t: Throwable): F[T]
  protected def handleWrappedError[T](rt: F[T])(
      h: PartialFunction[Throwable, F[T]]
  ): F[T]
  def handleError[T](rt: => F[T])(h: PartialFunction[Throwable, F[T]]): F[T] = {
    Try(rt) match {
      case Success(v)                     => handleWrappedError(v)(h)
      case Failure(e) if h.isDefinedAt(e) => h(e)
      case Failure(e)                     => raiseError(e)
    }
  }

  def eval[T](t: => T): F[T] = map(unit(()))(_ => t)
  def flatten[T](ffa: F[F[T]]): F[T] = flatMap[F[T], T](ffa)(identity)

  def timeout[T](fa: F[T], after: FiniteDuration): F[T]
}
