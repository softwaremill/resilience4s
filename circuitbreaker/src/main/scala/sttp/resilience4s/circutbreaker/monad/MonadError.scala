package sttp.resilience4s.circutbreaker.monad

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

  def fromTry[T](t: Try[T]): F[T] = t match {
    case Success(v) => unit(v)
    case Failure(e) => raiseError(e)
  }
}

object syntax {
  implicit final class MonadErrorOps[F[_], A](val r: F[A]) extends AnyVal {
    def map[B](f: A => B)(implicit ME: MonadError[F]): F[B] = ME.map(r)(f)
    def flatMap[B](f: A => F[B])(implicit ME: MonadError[F]): F[B] =
      ME.flatMap(r)(f)
    def >>[B](r2: F[B])(implicit ME: MonadError[F]): F[B] =
      ME.flatMap(r)(_ => r2)
    def handleError[T](h: PartialFunction[Throwable, F[A]])(
      implicit ME: MonadError[F]
    ): F[A] = ME.handleError(r)(h)
  }

  implicit final class MonadErrorValueOps[F[_], A](val v: A) extends AnyVal {
    def unit(implicit ME: MonadError[F]): F[A] = ME.unit(v)
  }
}
